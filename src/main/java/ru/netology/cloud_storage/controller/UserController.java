package ru.netology.cloud_storage.controller;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloud_storage.model.LoginRequest;
import ru.netology.cloud_storage.service.UserService;

@RestController //анотация для обработки HTTP запросов
@RequestMapping("/api/cloud") //базовый пусть для всех эдпоинтов
@AllArgsConstructor
public class UserController {

    //сервис для обработки запросов
    private final UserService userService;

    //эдпоинт для подачи заявки на вход
    @PostMapping("/entrance")
    public ResponseEntity<String> applyForLogin(@RequestBody LoginRequest login) {
        String token = userService.login(login.getEmail(), login.getPassword());
        //возврат тела с кодом 200 (ок)
        return ResponseEntity.ok(token);
    }

    //эдпоинт для подачи заявки на выход
    @PostMapping("/exit")
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
