package ru.netology.cloud_storage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloud_storage.model.LoginRequest;
import ru.netology.cloud_storage.model.TokenRequest;
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
        String token = userService.login(login.getLogin(), login.getPassword());
        //возврат тела с кодом 200 (ок)
        return ResponseEntity.ok(token);
    }

    //эдпоинт для подачи заявки на выход
    @PostMapping("/exit")
    public ResponseEntity<String> applyForLogout(@RequestBody TokenRequest token) {
        boolean result = userService.logout(token.getToken());

        if (result) {
            return ResponseEntity.ok("Вы вышли");
        } else {
            return ResponseEntity.status(401).body("Ошибка");
        }
    }
}
