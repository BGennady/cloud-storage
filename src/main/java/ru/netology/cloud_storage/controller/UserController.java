package ru.netology.cloud_storage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloud_storage.DTO.LoginResponse;
import ru.netology.cloud_storage.model.LoginRequest;
import ru.netology.cloud_storage.service.UserService;

@RestController //анотация для обработки HTTP запросов
@RequestMapping("/cloud") //базовый пусть для всех эдпоинтов
@AllArgsConstructor
public class UserController {

    //сервис для обработки запросов
    private final UserService userService;

    //эндпоинт для подачи заявки на вход
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> applyForLogin(@RequestBody LoginRequest login) {
        LoginResponse response = userService.login(login.getLogin(), login.getPassword());
        //возврат тела с кодом 200 (ок)
        return ResponseEntity.ok(response);
    }

    //эндпоинт для подачи заявки на выход
    @PostMapping("/logout")
    // прилетает заголовок типа: Authorization: Bearer ****
    public ResponseEntity<String> applyForLogout(Authentication auth){
        //получаю имя пользователя
        String username = auth.getName();

        boolean result = userService.logout();

        if (result) {
            return ResponseEntity.ok("Вы вышли");
        } else {
            return ResponseEntity.status(401).body("Ошибка");
        }
    }
}