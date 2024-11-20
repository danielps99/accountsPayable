package br.com.bdws.AccountsPayable.dto;

import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountPayableDto(Long id, LocalDate dueDate, LocalDate paymentDate, BigDecimal value, String description, AccountStatus status) {
}