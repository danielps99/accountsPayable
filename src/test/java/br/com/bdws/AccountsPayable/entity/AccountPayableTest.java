package br.com.bdws.AccountsPayable.entity;

import br.com.bdws.AccountsPayable.dto.UpdateStatusAccountPayableDto;
import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static br.com.bdws.AccountsPayable.entity.enums.AccountStatus.PAID;
import static br.com.bdws.AccountsPayable.entity.enums.AccountStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class AccountPayableTest {

    private static final LocalDate TODAY = LocalDate.now();

    private AccountPayable createAccountPayable(AccountStatus status) {
        AccountPayable a = new AccountPayable();
        a.setStatus(status);
        return a;
    }

    @Test
    public void shouldUpdateStatusToPaidAndPaymentDateToday() {
        UpdateStatusAccountPayableDto dto = UpdateStatusAccountPayableDto.builder()
                .paymentDate(TODAY)
                .status(PAID)
                .build();
        AccountPayable entity = createAccountPayable(PENDING);

        entity.updateStatus(dto);

        assertEquals(PAID, entity.getStatus());
        assertEquals(TODAY, entity.getPaymentDate());
    }

    @Test
    public void shouldUpdateStatusToPendentAndPaymentDateNull() {
        UpdateStatusAccountPayableDto dto = UpdateStatusAccountPayableDto.builder()
                .paymentDate(TODAY)
                .status(PENDING)
                .build();
        AccountPayable entity = createAccountPayable(PAID);

        entity.updateStatus(dto);

        assertEquals(PENDING, entity.getStatus());
        assertNull(entity.getPaymentDate());
    }
}