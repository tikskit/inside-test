package ru.tikskit.insidetest.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.insidetest.model.Auth;
import ru.tikskit.insidetest.repositories.AuthRepository;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthRepository authRepository;
    private final TokenGenerator tokenGenerator;

    @PostMapping("/auth")
    public @ResponseBody TokenDto addAuth(@RequestBody AuthDto authDto) {
        Auth auth = authRepository.findByNameAndPassword(authDto.getName(),
                authDto.getPassword()).orElseThrow(BadAuthenticationException::new);
        return new TokenDto(tokenGenerator.generate(auth.getName()));
    }
}
