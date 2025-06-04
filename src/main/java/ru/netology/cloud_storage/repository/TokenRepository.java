package ru.netology.cloud_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_storage.model.Token;

import java.util.Optional;

// репозиторий для связи приложения с БД через JPA
@Repository
public interface TokenRepository extends JpaRepository <Token, Long> {
       Optional<Token> findByToken(String token);
}
