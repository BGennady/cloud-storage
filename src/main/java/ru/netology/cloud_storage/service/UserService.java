package ru.netology.cloud_storage.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.cloud_storage.model.Token;
import ru.netology.cloud_storage.model.User;
import ru.netology.cloud_storage.repository.TokenRepository;
import ru.netology.cloud_storage.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
//класс для обработки
public class UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    // метод проверки login и password
    public String login(String login, String pass) {
        // ищем пользователя по логину
        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Пользовавтель не найден");
        }
        User user = userOpt.get();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // сверка захешированого пароля с тем, что хранится в базе
        if (!encoder.matches(pass, user.getPassword())) {
            throw new RuntimeException("Пароль не верный");
        }
        //генерирация нового токена
        String generatedToken = UUID.randomUUID().toString();

        //передача данных в таблицу
        Token token = new Token();
        token.setToken(generatedToken);
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());

        //сохранение данных
        tokenRepository.save(token);

        return generatedToken;
    }

    //метод для выхода
    public boolean logout() {

        User user = getCurrentUser();

        // получаю активного пользоваптеля из базы
        Optional<Token> tokenOpt = tokenRepository.findByUser(user);
        if (tokenOpt.isPresent()) {
            tokenRepository.delete(tokenOpt.get());
            return true;
        }
        ;
        return false;
    }

    //метод для получения текущего авторизованого пользователя из Spring Seqcurity
    public User getCurrentUser() {
        return ((User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal());
    }
}