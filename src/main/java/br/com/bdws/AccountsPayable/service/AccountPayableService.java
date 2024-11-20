package br.com.bdws.AccountsPayable.service;

import br.com.bdws.AccountsPayable.dto.*;
import br.com.bdws.AccountsPayable.entity.AccountPayable;
import br.com.bdws.AccountsPayable.repository.IAccountPayableRepository;
import com.opencsv.exceptions.CsvException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static br.com.bdws.AccountsPayable.entity.enums.AccountStatus.PAID;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class AccountPayableService {

    private final IAccountPayableRepository repository;

    private final CSVFileConverterService csvFileConverterService;

    public AccountPayable create(CreateAccountPayableDto dto) {
        AccountPayable entity = new AccountPayable(dto);
        repository.save(entity);
        return entity;
    }

    public AccountPayable update(UpdateAccountPayableDto dto) {
        AccountPayable entity = repository.findById(dto.id()).orElse(null);
        if (nonNull(entity)) {
            entity.update(dto);
            repository.save(entity);
        }
        return entity;
    }

    public AccountPayable updateStatus(UpdateStatusAccountPayableDto dto) {
        AccountPayable entity = repository.findById(dto.id()).orElse(null);
        if (nonNull(entity)) {
            entity.updateStatus(dto);
            repository.save(entity);
        }
        return entity;
    }

    public PageResponse<AccountPayable> filter(LocalDate dueDate, String description, Pageable pageable) {
        if (nonNull(dueDate)) {
            return new PageResponse<>(repository.findByDueDateAndDescriptionContainingIgnoreCase(dueDate, description, pageable));
        }
        return new PageResponse<>(repository.findByDescriptionContainingIgnoreCase(description, pageable));
    }

    public AccountPayable findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada. ID: " + id));
    }

    public BigDecimal sumTotalPaidByRange(LocalDate start, LocalDate end) {
        List<AccountPayable> accounts = repository.findByStatusAndPaymentDateBetween(PAID,start, end);
        return accounts.stream().map(AccountPayable::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<AccountPayable> loadFromCsvFile(MultipartFile csv) {
        if (csv.isEmpty()) {
            throw new RuntimeException("Body com csv_file Vazio. Envie no body da requisição um arquivo .csv.");
        }
        try {
            List<AccountPayableFromCsvDto> dtos = csvFileConverterService.convertToAccountPayableFromCsvDto(csv);
            List<AccountPayable> entities = dtos.stream()
                    .map(AccountPayable::new)
                    .toList();

            repository.saveAll(entities);
            return entities;
        } catch (Exception e) {
            if (e.getCause() instanceof CsvException) {
                throw new RuntimeException("Arquivo csv com formato inválido.");
            }
            throw new RuntimeException(e);
        }
    }
}
