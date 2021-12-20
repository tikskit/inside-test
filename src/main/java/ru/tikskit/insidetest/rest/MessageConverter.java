package ru.tikskit.insidetest.rest;

import ru.tikskit.insidetest.model.Message;

public interface MessageConverter {
    MessageDto convert2Dto(Message message);
}
