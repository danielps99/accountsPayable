package br.com.bdws.AccountsPayable.entity;

import br.com.bdws.AccountsPayable.dto.AccountPayableFromCsvDto;
import br.com.bdws.AccountsPayable.dto.CreateAccountPayableDto;
import br.com.bdws.AccountsPayable.dto.UpdateAccountPayableDto;
import br.com.bdws.AccountsPayable.dto.UpdateStatusAccountPayableDto;
import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.bdws.AccountsPayable.entity.enums.AccountStatus.PAID;
import static br.com.bdws.AccountsPayable.entity.enums.AccountStatus.PENDING;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "accounts_payable")
public class AccountPayable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dueDate;

    private LocalDate paymentDate;

    private BigDecimal value;

    private String description;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public AccountPayable(CreateAccountPayableDto dto) {
        dueDate = dto.dueDate();
        value = dto.value();
        description = dto.description();
        status = PENDING;
    }

    public AccountPayable(AccountPayableFromCsvDto dto) {
        dueDate = dto.getDueDate();
        value = dto.getValue();
        description = dto.getDescription();
        status = dto.getStatus();
        paymentDate = dto.getPaymentDate();
    }

    public void update(UpdateAccountPayableDto dto) {
        dueDate = dto.dueDate();
        value = dto.value();
        description = dto.description();
    }

    public void updateStatus(UpdateStatusAccountPayableDto dto) {
        if (PENDING.equals(dto.status()) && !PENDING.equals(status)) {
            status = PENDING;
            paymentDate = null;
        }
        if (PAID.equals(dto.status()) && !PAID.equals(status)) {
            status = PAID;
            paymentDate =  dto.paymentDate();
        }
    }
}
