package ru.netology.cloud_storage.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloud_storage.model.Token;
import ru.netology.cloud_storage.model.User;
import ru.netology.cloud_storage.repository.TokenRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    final private TokenRepository tokenRepository;

    // метод для обработки запроса от пользователя
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //проверка, что на вход
        String path = request.getRequestURI();
        if (path.equals("cloud/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        //получает заголовок из запроса
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");
            Optional<Token> optionalToken = tokenRepository.findByToken(token);

            if (optionalToken.isPresent()) {
                User user = optionalToken.get().getUser();
                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}