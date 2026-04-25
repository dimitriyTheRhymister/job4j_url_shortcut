package ru.job4j.urlshortcut.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.job4j.urlshortcut.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Пропускаем без авторизации (PUBLIC ENDPOINTS)
        if (path.equals("/registration") ||
                path.equals("/auth") ||
                path.equals("/auth-test") ||
                path.equals("/ping") ||
                path.equals("/check-login") ||
                path.startsWith("/h2-console") ||
                path.startsWith("/redirect/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Проверяем Authorization header для защищённых эндпоинтов
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtService.validateToken(token)) {
                String login = jwtService.extractLogin(token);
                request.setAttribute("login", login);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Если авторизация не прошла
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");
    }
}
