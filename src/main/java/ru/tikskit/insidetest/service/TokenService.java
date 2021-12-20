package ru.tikskit.insidetest.service;

/**
 * Сервис для работы с jws-токенами
 */
public interface TokenService {
    /**
     * Сгенерировать токен с именем пользователя
     * @param name Имя пользователя
     * @return возвращает Jws токен
     */
    String generate(String name);

    /**
     * Проверяет токен
     * @param token Jws-токен
     * @return @True, если токен валидный, иначе @False.
     */
    boolean checkToken(String token);
}
