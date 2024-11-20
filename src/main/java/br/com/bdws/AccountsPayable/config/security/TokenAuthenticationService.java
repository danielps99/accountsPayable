package br.com.bdws.AccountsPayable.config.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class TokenAuthenticationService {

    private static final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(1);
    private static final String HEADER_STRING = "Authorization";
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt-secret}")
    private String secret;


    public void authenticate(HttpServletResponse response, LoginDto dto) {
                Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.username(),
                dto.password(),
                Collections.emptyList()));
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(dto.username())
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusMillis(EXPIRATION_TIME)))
                .build();
        try {
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            signedJWT.sign(generateKey());
            response.addHeader(HEADER_STRING, signedJWT.serialize());
        } catch (JOSEException e) {
            log.error("authenticate error: {}", e.getMessage());
            response.addHeader(HEADER_STRING, "");
        }
    }

    private JWSSigner generateKey() throws KeyLengthException {
        return new MACSigner(secret);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        try {
            String user = getUserFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user);
            Collection<? extends GrantedAuthority> authorities =
                    userDetails.getAuthorities();
            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        } catch (ParseException | NullPointerException | StringIndexOutOfBoundsException e) {
            log.error("getAuthentication error: {}", e.getMessage());
            return null;
        }
    }

    private String getUserFromToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token.substring(7));
        return signedJWT.getJWTClaimsSet()
                .getClaim( JWTClaimNames.SUBJECT).toString();
    }
}