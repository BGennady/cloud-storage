package ru.netology.cloud_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.cloud_storage.model.Token;
import ru.netology.cloud_storage.model.User;

import java.util.List;
import java.util.Optional;

// репозиторий для связи приложения с БД через JPA
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    Optional<Token> findByUser(User user);

    @Transactional
    @Modifying
    List<Token> deleteByUser(User user);
}