package ru.tikskit.insidetest.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class UserNotfoundException extends RuntimeException{
}
