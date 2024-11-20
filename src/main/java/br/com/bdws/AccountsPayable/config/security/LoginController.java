package br.com.bdws.AccountsPayable.config.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
@RequiredArgsConstructor
@Tag(name = "Login", description = "Endpoint usado para autenticar usuário.")
public class LoginController {

    private final TokenAuthenticationService service;

    @PostMapping
    @Operation(
            summary =  "Autenticação do usuário.",
            description = "Autentica usuário baseado no usuário e senha.")
    public void authenticate(HttpServletResponse response, @RequestBody LoginDto dto) {
        service.authenticate(response, dto);
    }
}
