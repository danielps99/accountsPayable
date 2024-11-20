package br.com.bdws.AccountsPayable.controller;

import br.com.bdws.AccountsPayable.dto.CreateAccountPayableDto;
import br.com.bdws.AccountsPayable.dto.PageResponse;
import br.com.bdws.AccountsPayable.dto.UpdateAccountPayableDto;
import br.com.bdws.AccountsPayable.dto.UpdateStatusAccountPayableDto;
import br.com.bdws.AccountsPayable.entity.AccountPayable;
import br.com.bdws.AccountsPayable.service.AccountPayableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account-payable")
@Tag(name = "Contas a pagar", description = "Endpoint usado para salvar, editar e listar contas a pagar.")
@RequiredArgsConstructor
public class AccountsPayableController {

    private final AccountPayableService service;

    @PostMapping
    @Operation(
            summary =  "Salva conta a pagar",
            description = "Salva conta a pagar")
    public ResponseEntity<AccountPayable> create(@RequestBody CreateAccountPayableDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping
    @Operation(
            summary =  "Atualiza conta a pagar",
            description = "Atualiza conta a pagar")
    public ResponseEntity<AccountPayable> update(@RequestBody UpdateAccountPayableDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }

    @PutMapping("status")
    @Operation(
            summary =  "Atualiza status da conta a pagar",
            description = "Atualiza status da conta a pagar")
    public ResponseEntity<AccountPayable> updateStatus(@RequestBody UpdateStatusAccountPayableDto dto) {
        return ResponseEntity.ok(service.updateStatus(dto));
    }

    @GetMapping
    @Operation(
            summary =  "Busca conta a pagar por id",
            description = "Busca conta a pagar por id")
    public ResponseEntity<AccountPayable> findById(@RequestParam Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("filter")
    @Operation(
            summary =  "Busca contas a pagar",
            description = "Busca contas a pagar filtrando por data de vencimento e descrição")
    public ResponseEntity<PageResponse<AccountPayable>> filter(
            @RequestParam (required = false) LocalDate dueDate,
            @RequestParam(defaultValue = "") String description,
            @PageableDefault(sort = "dueDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.filter(dueDate, description, pageable));
    }

    @GetMapping("total-paid")
    @Operation(
            summary =  "Busca somatório das contas a pagar",
            description = "Busca somatório das contas a pagar pagas de acordo com o período informado")
    public ResponseEntity<BigDecimal> sumTotalPaidByRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(service.sumTotalPaidByRange(start, end));
    }

    @PostMapping("/upload-csv")
    @Operation(
            summary = "Importa contas a pagar",
            description = "Importa contas a pagar a partir de um arquivo csv. Abaixo segue o formato de csv esperado com 2 registros:" +
                    "<br> " +
                    "<br>due_date,payment_date,value,description,status" +
                    "<br>2024-11-30,,215.30,energia da casa,PENDING" +
                    "<br>2024-11-29,2024-11-12,10015.30,luz do escritório,PAID")
    public ResponseEntity<List<AccountPayable>> uploadCsv(@RequestParam("csv_file") MultipartFile csv) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.loadFromCsvFile(csv));
    }
}
