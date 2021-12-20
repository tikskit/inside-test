package ru.tikskit.insidetest.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса TokenService
 */
@Service
public class TokenServiceImpl implements TokenService {
    private static final String SECRET = "littleyellowguitar";
    private static final String CLAIM_NAME_KEY = "name";


    @Override
    public String generate(String name) {
        return Jwts.builder()
                .claim(CLAIM_NAME_KEY, name)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    @Override
    public boolean checkToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
