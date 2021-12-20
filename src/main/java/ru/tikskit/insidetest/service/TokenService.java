package ru.tikskit.insidetest.service;

public interface TokenService {
    String generate(String name);

    boolean checkToken(String token);
}
