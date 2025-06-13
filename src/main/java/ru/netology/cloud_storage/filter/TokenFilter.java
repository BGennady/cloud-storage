package ru.netology.cloud_storage.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.netology.cloud_storage.repository.TokenRepository;

import java.io.IOException;


@Component //анотация что бы, Spring сам подхватывал фильтр
@AllArgsConstructor

//класс-фильтр будет перехватывать http-запросы и проверять их до контролера
public class TokenFilter implements Filter {
    private final TokenRepository tokenRepository;

    //переопредлеляю метод doFilter
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        //привожу переменные request и response к нужному типу
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //запросы на вход и выход пропускаем без проверки
        String path = httpRequest.getRequestURI();
        if(path.equals("/api/cloud/entrance") || path.equals("/api/cloud/exit")){
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        //проверяю заголовок, что не null и не начинается на Bearer с пробелом
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
            httpResponse.getWriter().write("Unauthorized: отсутсвует заголовок Bearer");
            return;
        }

        //обрезаю Bearer
        String token = authHeader.substring(7);

        //если токена нет/нет в базе выкидываем ошибку 401 Unauthorization
        if (token == null || token.isEmpty() || tokenRepository.findByToken(token).isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
            httpResponse.getWriter().write("Unauthorized: токен отсвуствует или недействителен");
            return;
        }
        //если все ОК запрос уходит обработку (по цепочке фильтров)
        chain.doFilter(request, response);
    }
}