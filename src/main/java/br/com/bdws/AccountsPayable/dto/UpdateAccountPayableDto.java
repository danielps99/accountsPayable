package br.com.bdws.AccountsPayable.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UpdateAccountPayableDto(Long id, LocalDate dueDate, BigDecimal value, String description) {
}