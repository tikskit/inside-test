package ru.tikskit.insidetest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.insidetest.model.Auth;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByNameAndPassword(String name, String password);
}
