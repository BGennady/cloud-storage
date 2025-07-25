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
        if (path.equals("/api/cloud/entrance")) {
            filterChain.doFilter(request, response);
            return;
        }

        //получает заголовок из запроса
        String header = request.getHeader("Authorization");

        // проверка, что не пустой и начинается и начинается с "Bearer"
        if (header != null && header.startsWith("Bearer ")) {
            //отсекает заголовок Bearer (меняется на ничего)
            String token = header.replace("Bearer ", "");

            Optional<Token> optionalToken = tokenRepository.findByToken(token);
            //проверка, что токен не пуст
            if (optionalToken.isPresent()) {
                //берет user по этому токену
                User user = optionalToken.get().getUser();

                //объект Authentication с пользователем и пустыстыми правами
                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

                //оборачивает user и учетные данные (Authentication) в SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        //при любом варианте, передает дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}
