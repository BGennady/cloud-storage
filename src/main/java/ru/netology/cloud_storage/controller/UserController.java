package ru.netology.cloud_storage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    // прилетает заголовок типа: Authorization: Bearer ****
    public ResponseEntity<String> applyForLogout(@RequestHeader("Authorization") String authToken) {
        //отсекается заголовок Bearer (меняется на ничего)
        String token = authToken.replace("Bearer ", "");

        boolean result = userService.logout(token);

        if (result) {
            return ResponseEntity.ok("Вы вышли");
        } else {
            return ResponseEntity.status(401).body("Ошибка");
        }
    }
}
