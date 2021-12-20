package ru.tikskit.insidetest.rest;

import ru.tikskit.insidetest.model.Message;

/**
 * Конвертен сообщений из сущности в Dto
 */
public interface MessageConverter {
    /**
     * Сконвертировать сущность в Dto
     * @param message сущность
     * @return возвращает Dto, либо null, если message null
     */
    MessageDto convert2Dto(Message message);
}
