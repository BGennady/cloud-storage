package ru.netology.cloud_storage.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String email;
    @JsonProperty("auth-token")
    private String authToken;
}