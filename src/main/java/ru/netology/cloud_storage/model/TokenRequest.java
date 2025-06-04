package ru.netology.cloud_storage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

//класс для упаковки запросов клиента-token
public class TokenRequest {
    private String token;
}
