package ru.job4j.urlshortcut.service;

import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.entity.Site;
import ru.job4j.urlshortcut.repository.SiteRepository;

import java.util.Optional;

@Service
public class AuthService {

    private final SiteRepository siteRepository;
    private final JwtService jwtService;

    public AuthService(SiteRepository siteRepository, JwtService jwtService) {
        this.siteRepository = siteRepository;
        this.jwtService = jwtService;
    }

    public String authenticate(String login, String password) {
        // Ищем сайт по логину и паролю (в реальном проекте пароль нужно хешировать!)
        Optional<Site> siteOpt = siteRepository.findByLoginAndPasswordHash(login, password);

        if (siteOpt.isPresent()) {
            // Генерируем JWT токен
            return jwtService.generateToken(login);
        }

        return null;  // Неправильный логин или пароль
    }
}
