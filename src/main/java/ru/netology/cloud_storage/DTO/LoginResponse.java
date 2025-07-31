package ru.netology.cloud_storage.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    @JsonProperty("auth-token")
    private String authToken;
}