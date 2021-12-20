package ru.tikskit.insidetest.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class TokenGeneratorImpl implements TokenGenerator {
    @Override
    public String generate(String username) {
        return Jwts.builder()
                .setPayload(username)
                .signWith(SignatureAlgorithm.HS512, "littleyellowguitar")
                .compact();
    }
}
