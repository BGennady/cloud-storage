package ru.netology.cloud_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_storage.model.User;

import java.util.Optional;

// репозиторий для связи приложения с БД через JPA
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // метод для поиска пользователя по логину
    Optional<User> findByLogin(String login);
}