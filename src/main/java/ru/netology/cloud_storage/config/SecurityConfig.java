package ru.netology.cloud_storage.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.netology.cloud_storage.repository.TokenRepository;
@AllArgsConstructor
@Configuration
public class SecurityConfig {
    private final TokenRepository tokenRepository;

    @Bean
    // типовой конфигурационный метод для настройки фильтроа безопасности у входящих HTTP-запросов
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // отключает CSRF — иначе Postman может блокироваться
                .httpBasic(httpBasic -> httpBasic.disable()) //отключение базовой аутентефикации
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/cloud/login").permitAll()
                        // разрешаем свободный доступ к /api/public/**
                        .anyRequest().authenticated() // все остальные — только для авторизованных
                )
                .addFilterBefore(new JwtTokenFilter(tokenRepository),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
