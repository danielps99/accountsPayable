package br.com.bdws.AccountsPayable.config.security;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDto (
        @Schema(description = "Usuário", example = "admin")
        String username,
        @Schema(description = "Password", example = "admin")
        String password)
{ }
