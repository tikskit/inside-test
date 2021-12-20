package ru.tikskit.insidetest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.insidetest.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop10ByOrderByIdDesc();
}
