package ru.netology.cloud_storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    // типовой конфигурационный метод для настройки фильтроа безопасности у входящих HTTP-запросов
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // отключает CSRF — иначе Postman может блокироваться
                .httpBasic(httpBasic ->httpBasic.disable()) //отключение базовой аутентефикации
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/cloud/entrance").permitAll()
                        // разрешаем свободный доступ к /api/public/**
                        .anyRequest().authenticated() // все остальные — только для авторизованных
                );


        return http.build();
    }
}
