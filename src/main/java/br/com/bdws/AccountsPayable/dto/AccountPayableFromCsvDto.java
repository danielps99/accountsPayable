package br.com.bdws.AccountsPayable.dto;

import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AccountPayableFromCsvDto{

    @CsvBindByName(column = "due_date")
    @CsvDate("yyyy-MM-dd")
    private LocalDate dueDate;

    @CsvBindByName(column = "payment_date")
    @CsvDate("yyyy-MM-dd")
    private LocalDate paymentDate;

    @CsvBindByName(column = "value")
    private BigDecimal value;

    @CsvBindByName(column = "description")
    private String description;

    @CsvCustomBindByName(column = "status", converter = AccountStatusConverter.class)
    private AccountStatus status;
}