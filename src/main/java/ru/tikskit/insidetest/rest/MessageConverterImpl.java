package ru.tikskit.insidetest.rest;

import org.springframework.stereotype.Component;
import ru.tikskit.insidetest.model.Message;

import java.util.Objects;

@Component
public class MessageConverterImpl implements MessageConverter {
    @Override
    public MessageDto convert2Dto(Message message) {
        Objects.requireNonNull(message);

        return new MessageDto(message.getAuth().getName(), message.getMessage());
    }
}
