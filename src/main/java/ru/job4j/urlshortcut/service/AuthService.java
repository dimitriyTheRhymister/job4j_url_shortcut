package ru.job4j.urlshortcut.service;

import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.repository.SiteRepository;

@Service
public class AuthService {

    private final SiteRepository siteRepository;
    private final JwtService jwtService;

    public AuthService(SiteRepository siteRepository, JwtService jwtService) {
        this.siteRepository = siteRepository;
        this.jwtService = jwtService;
    }

    public String authenticate(String login, String password) {
        return siteRepository.findByLoginAndPasswordHash(login, password)
                .map(site -> jwtService.generateToken(login))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    }
}
