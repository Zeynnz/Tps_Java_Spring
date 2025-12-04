package org.coiffet.tp1.tokenJWT;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class TokenGenerator {

    private final TokenValidator tokenValidator;

    public TokenGenerator(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date tokenCreationDate = new Date();
        long jwtExpirationMs = 1000 * 600;
        Date tokenExpirationDate = new Date(tokenCreationDate.getTime() + jwtExpirationMs);

        SecretKey key = tokenValidator.getKey();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(tokenCreationDate)
                .expiration(tokenExpirationDate)
                .signWith(key)
                .compact();
    }
}
