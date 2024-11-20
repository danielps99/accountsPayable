package br.com.bdws.AccountsPayable.service;

import br.com.bdws.AccountsPayable.dto.CreateAccountPayableDto;
import br.com.bdws.AccountsPayable.dto.UpdateAccountPayableDto;
import br.com.bdws.AccountsPayable.dto.UpdateStatusAccountPayableDto;
import br.com.bdws.AccountsPayable.entity.AccountPayable;
import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;
import br.com.bdws.AccountsPayable.repository.IAccountPayableRepository;
import br.com.bdws.AccountsPayable.DiskMultipartFileUtil;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static br.com.bdws.AccountsPayable.entity.enums.AccountStatus.PAID;
import static br.com.bdws.AccountsPayable.entity.enums.AccountStatus.PENDING;
import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
class AccountPayableServiceTest {

    @Autowired
    private AccountPayableService service;

    @MockBean
    private IAccountPayableRepository repository;

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate TOMORROW = TODAY.plusDays(1);
    private static final PageRequest PAGE_REQUEST = PageRequest.of(1, 1);
    private static final Page<AccountPayable> PAGE = new PageImpl<>(new ArrayList<>(), PAGE_REQUEST, 1);

    ArgumentCaptor<AccountPayable> accountCaptor = ArgumentCaptor.forClass(AccountPayable.class);
    ArgumentCaptor<List<AccountPayable>> accountCaptorList = ArgumentCaptor.forClass(List.class);

    private AccountPayable createAccountPayable(AccountStatus status) {
        return createAccountPayable(status, null);
    }

    private AccountPayable createAccountPayable(AccountStatus status, BigDecimal value) {
        AccountPayable a = new AccountPayable();
        a.setStatus(status);
        a.setValue(value);
        return a;
    }

    private DiskMultipartFileUtil loadCsvFileFromLocalDiskForTest() {
        Resource resource = resourceLoader.getResource("classpath:accountPayableExamples.csv");
        try {
            return new DiskMultipartFileUtil(resource.getFile(), "file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldCreateAccountWithStatusPendingAndPaymentDateNull() {
        CreateAccountPayableDto dto = CreateAccountPayableDto.builder()
                .description("Descrição conta")
                .value(BigDecimal.TEN)
                .dueDate(TODAY)
                .build();

        service.create(dto);

        verify(repository).save(accountCaptor.capture());
        AccountPayable entity = accountCaptor.getValue();

        assertEquals("Descrição conta", entity.getDescription());
        assertEquals(BigDecimal.TEN, entity.getValue());
        assertEquals(TODAY, entity.getDueDate());
        assertEquals(PENDING, entity.getStatus());
        assertNull(entity.getPaymentDate());
    }

    @Test
    public void shouldNotSaveWhenFindByIdReturnsNull() {
        UpdateAccountPayableDto dto = UpdateAccountPayableDto.builder()
                .id(1L)
                .description("Descrição conta atualizada")
                .value(ONE)
                .dueDate(TOMORROW)
                .build();

        service.update(dto);

        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldUpdateDescriptionValueAndDueDate() {
        AccountPayable fromDb = createAccountPayable(PENDING);
        UpdateAccountPayableDto dto = UpdateAccountPayableDto.builder()
                .id(1L)
                .description("Descrição conta atualizada")
                .value(ONE)
                .dueDate(TOMORROW)
                .build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(fromDb));

        service.update(dto);

        verify(repository).findById(anyLong());
        verify(repository).save(accountCaptor.capture());
        AccountPayable entity = accountCaptor.getValue();

        assertEquals("Descrição conta atualizada", entity.getDescription());
        assertEquals(ONE, entity.getValue());
        assertEquals(TOMORROW, entity.getDueDate());
        assertEquals(PENDING, entity.getStatus());
        assertNull(entity.getPaymentDate());

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldUpdateStatusWhenFindByIdReturnsNull() {
        AccountPayable fromDb = createAccountPayable(PENDING);
        UpdateStatusAccountPayableDto dto = UpdateStatusAccountPayableDto.builder()
                .id(1L)
                .status(PAID)
                .build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(fromDb));

        service.updateStatus(dto);

        verify(repository).findById(anyLong());
        verify(repository).save(accountCaptor.capture());
        AccountPayable entity = accountCaptor.getValue();

        assertEquals(PAID, entity.getStatus());
        assertNull(entity.getPaymentDate());

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldUpdateStatusToPaid() {
        AccountPayable fromDb = createAccountPayable(PENDING);
        UpdateStatusAccountPayableDto dto = UpdateStatusAccountPayableDto.builder()
                .id(1L)
                .status(PAID)
                .build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(fromDb));

        service.updateStatus(dto);

        verify(repository).findById(anyLong());
        verify(repository).save(accountCaptor.capture());
        AccountPayable entity = accountCaptor.getValue();

        assertEquals(PAID, entity.getStatus());
        assertNull(entity.getPaymentDate());

        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldFindByDescriptionContainingIgnoreCaseWhenDueDateIsNull() {
        when(repository.findByDescriptionContainingIgnoreCase(anyString(), any())).thenReturn(PAGE);

        service.filter(null, "", PageRequest.of(1, 1));

        verify(repository).findByDescriptionContainingIgnoreCase(anyString(), any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldFindByDueDateAndDescriptionContainingIgnoreCaseWhenDueDateAndDescriptionIsFilled() {
        when(repository.findByDueDateAndDescriptionContainingIgnoreCase(any(), anyString(), any())).thenReturn(PAGE);

        service.filter(TODAY, "some description", PageRequest.of(1, 1));

        verify(repository).findByDueDateAndDescriptionContainingIgnoreCase(any(), anyString(), any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldSumTotalPaidByRange() {
        List<AccountPayable> accounts = Arrays.asList(
                createAccountPayable(PAID, new BigDecimal("10.45")),
                createAccountPayable(PAID, new BigDecimal("1253.56")));
        when(repository.findByStatusAndPaymentDateBetween(any(), any(), any())).thenReturn(accounts);

        BigDecimal total = service.sumTotalPaidByRange(TODAY, TOMORROW);

        assertEquals(new BigDecimal("1264.01"), total);
        verify(repository).findByStatusAndPaymentDateBetween(any(), any(), any());
        verifyNoMoreInteractions(repository);
    }

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void shouldLoadFromCsvFile() {

        service.loadFromCsvFile(loadCsvFileFromLocalDiskForTest());

        verify(repository).saveAll(accountCaptorList.capture());

        List<AccountPayable> entities = accountCaptorList.getValue();
        assertEquals(2, entities.size());
        AccountPayable entity0 = entities.get(0);
        AccountPayable entity1 = entities.get(1);
        assertEquals(LocalDate.of(2024, 12, 30), entity0.getDueDate());
        assertNull(entity0.getPaymentDate());
        assertEquals(new BigDecimal("215.30"), entity0.getValue());
        assertEquals("energia da casa", entity0.getDescription());
        assertEquals(PENDING, entity0.getStatus());
        assertEquals(LocalDate.of(2024, 12, 15), entity1.getDueDate());
        assertEquals(LocalDate.of(2024, 11, 25),entity1.getPaymentDate());
        assertEquals(new BigDecimal("10015.30"), entity1.getValue());
        assertEquals("luz do escritorio", entity1.getDescription());
        assertEquals(PAID, entity1.getStatus());
    }
}
