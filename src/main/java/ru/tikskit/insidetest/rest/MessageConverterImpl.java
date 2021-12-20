package ru.tikskit.insidetest.rest;

import org.springframework.stereotype.Component;
import ru.tikskit.insidetest.model.Message;

/**
 * Реализация конвертера MessageConverter
 */
@Component
public class MessageConverterImpl implements MessageConverter {
    @Override
    public MessageDto convert2Dto(Message message) {
        if (message == null) {
            return null;
        } else {
            return new MessageDto(message.getAuth().getName(), message.getMessage());
        }

    }
}
