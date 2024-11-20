package br.com.bdws.AccountsPayable.dto;

import br.com.bdws.AccountsPayable.entity.enums.AccountStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateStatusAccountPayableDto(Long id, LocalDate paymentDate, AccountStatus status) {
}