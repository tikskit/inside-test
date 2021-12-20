package ru.tikskit.insidetest.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.tikskit.insidetest.model.Auth;
import ru.tikskit.insidetest.model.Message;
import ru.tikskit.insidetest.repositories.AuthRepository;
import ru.tikskit.insidetest.repositories.MessageRepository;
import ru.tikskit.insidetest.service.TokenService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Рест контроллер должен")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthRepository authRepository;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private MessageRepository messageRepository;
    @Autowired
    private MessageConverter messageConverter;

    @Test
    @DisplayName("возвращать в методе POST /auth токен в виде JSON, если пользователь есть в БД")
    public void shouldReturnTokenInJsonWhenUserExists() throws Exception {
        final String token = "x-token";
        when(tokenService.generate("test")).thenReturn(token);
        when(authRepository.findByNameAndPassword("test", "000"))
                .thenReturn(Optional.of(new Auth("test", "000")));

        AuthDto authDto = new AuthDto("test", "000");
        ObjectMapper mapper = new ObjectMapper();

        String authDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(authDto);

        TokenDto tokenDto = new TokenDto(token);
        String tokenDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tokenDto);

        mockMvc.perform(
                post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authDtoJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(tokenDtoJson));
    }

    @Test
    @DisplayName("возвращать статус 400, если в метод POST /auth в body переданы данные пользователя, который не существует")
    public void shouldReturn400WhenUserDoesnExist() throws Exception {
        when(authRepository.findByNameAndPassword(any(), any())).thenReturn(Optional.empty());
        AuthDto authDto = new AuthDto("test", "000");
        ObjectMapper mapper = new ObjectMapper();
        String authDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(authDto);

        mockMvc.perform(
                post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authDtoJson)
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("сохранять сообщение в БД, если в метод POST /message передать JSON с валидным сообщением и токеном в хедере")
    public void shouldSaveMessageInDBWhenMessageDtoIsValidAndHeaderPresents() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        when(tokenService.checkToken(any())).thenReturn(true);
        when(authRepository.findByName(any())).thenReturn(Optional.of(new Auth(10L, "name", "pass")));
        when(messageRepository.save(any())).thenReturn(new Message());

        MessageDto messageDto = new MessageDto("name", "regular message");
        String messageDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageDto);

        mockMvc.perform(
                post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageDtoJson)
                        .header("token", "sometoken")
        )
                .andExpect(status().isOk());

        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("Возвращать код 400, если в POST /message не передать токен в хедере")
    public void shouldReturn400IfTokenInHeaderAbsents() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        MessageDto messageDto = new MessageDto("name", "regular message");
        String messageDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageDto);

        mockMvc.perform(
                post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageDtoJson)
        )
                .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("Возвращать код 400, если в POST /message токен в хедере недействительный")
    public void shouldReturn400IfTokenIsInvalid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        when(tokenService.checkToken(any())).thenReturn(false);

        MessageDto messageDto = new MessageDto("name", "regular message");
        String messageDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageDto);

        mockMvc.perform(
                post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageDtoJson)
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Возвращать код 400, если в POST /message не передать Message в JSON")
    public void shouldReturn400NoMessageJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(
                post("/message")
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("возвращать 10 последних сообщений, если POST /message сообщение 'history 10'")
    public void shouldReturnLast10Messages() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<Message> messages = new ArrayList<>();
        Auth auth = new Auth("someuser", "somepassword");
        for (int i = 0; i < 10; i++) {
            messages.add(new Message(i, auth, String.format("message %s", i)));
        }

        when(tokenService.checkToken(any())).thenReturn(true);
        when(messageRepository.findTop10ByOrderByIdDesc()).thenReturn(messages);

        MessageDto messageDto = new MessageDto("name", "history 10");
        String messageDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageDto);

        String historyMessagesDtoJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                messages.stream().map(messageConverter::convert2Dto).collect(Collectors.toList())
        );

        mockMvc.perform(
                post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageDtoJson)
                        .header("token", "sometoken")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(historyMessagesDtoJson));

    }
}