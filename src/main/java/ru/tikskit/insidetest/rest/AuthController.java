package ru.tikskit.insidetest.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tikskit.insidetest.model.Auth;
import ru.tikskit.insidetest.model.Message;
import ru.tikskit.insidetest.repositories.AuthRepository;
import ru.tikskit.insidetest.repositories.MessageRepository;
import ru.tikskit.insidetest.service.TokenService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthRepository authRepository;
    private final MessageRepository messageRepository;
    private final TokenService tokenService;
    private final MessageConverter messageConverter;

    @PostMapping("/auth")
    public @ResponseBody TokenDto addAuth(@RequestBody AuthDto authDto) {
        Auth auth = authRepository.findByNameAndPassword(authDto.getName(),
                authDto.getPassword()).orElseThrow(BadAuthenticationException::new);
        return new TokenDto(tokenService.generate(auth.getName()));
    }

    @PostMapping("/message")
    public @ResponseBody List<MessageDto> addMessage(@RequestBody MessageDto messageDto,
                                                     @RequestHeader("token") String token) {
        if (tokenService.checkToken(token)) {
            if ("history 10".equals(messageDto.getMessage())) {
                return getLast10Messages();
            } else {
                storeMessage(messageDto);
                return null;
            }
        } else {
            throw new BadTokenException();
        }
    }

    private void storeMessage(MessageDto messageDto) {
        Auth auth = authRepository.findByName(messageDto.getName()).orElseThrow(UserNotfoundException::new);
        Message message = new Message();
        message.setAuth(auth);
        message.setMessage(messageDto.getMessage());
        message = messageRepository.save(message);
    }

    private List<MessageDto> getLast10Messages() {
        List<Message> last10 = messageRepository.findTop10ByOrderByIdDesc();
        return last10.stream().map(messageConverter::convert2Dto).collect(Collectors.toList());
    }


}
