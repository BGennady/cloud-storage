package ru.netology.cloud_storage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

//класс для упаковки запроса клиента-log/pass
public class LoginRequest {
    private String email;
    private String password;
}
