package ru.netology.cloud_storage.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.netology.cloud_storage.DTO.LoginResponse;
import ru.netology.cloud_storage.model.Token;
import ru.netology.cloud_storage.model.User;
import ru.netology.cloud_storage.repository.TokenRepository;
import ru.netology.cloud_storage.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService; //тестируемый класс
    private UserRepository userRepository; //заглушка для репощитория
    private TokenRepository tokenRepository; //заглушка для репозитория
    private final String token = "a12345b";
    private final Long userId = 101L;
    private final String login = "admin";
    private final String pass = "pass123";

    private User testUser;
    private Token testToken;

    private BCryptPasswordEncoder encoder;


    @BeforeEach
    void init() {
        userRepository = mock(UserRepository.class);
        tokenRepository = mock((TokenRepository.class));
        //объект userService
        userService = new UserService(userRepository, tokenRepository);

        encoder = new BCryptPasswordEncoder();


        //хеширование полученого пароля
        String encoderPass = encoder.encode(pass);

        testUser = new User(userId, login, encoderPass);
        testToken = new Token();
        testToken.setToken(token);
        testToken.setUser(testUser);
    }

    @AfterEach
    void clean() {
    }

    @Test
    public void loginTest() {

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(testUser));

        // запуск метода
        LoginResponse response = userService.login(login, pass);
        String token = response.getAuthToken();

        // проверка, что на null и empty
        assertEquals(true, token != null && !token.isEmpty());

        // проверка, на сохранение
        verify(tokenRepository).save(any());
    }

    @Test
    public void logoutTest() {

        //создает проверенного (аутентефицированого пользтователя) в SecurityContext
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // запуск метода
        boolean result = userService.logout();

        // проверка результата
        assertEquals(true, result);

        //проверка удаления
        verify(tokenRepository).deleteByUser(testUser);
    }
}

