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
        System.out.println("=== AUTHENTICATION DEBUG ===");
        System.out.println("Login received: '" + login + "'");
        System.out.println("Password received: '" + password + "'");

        Optional<Site> siteOpt = siteRepository.findByLogin(login);

        if (siteOpt.isEmpty()) {
            System.out.println("ERROR: Site not found for login: " + login);
            return null;
        }

        Site site = siteOpt.get();
        System.out.println("Site found: " + site.getSiteName());
        System.out.println("Stored password hash: '" + site.getPasswordHash() + "'");
        System.out.println("Password matches: " + site.getPasswordHash().equals(password));

        if (site.getPasswordHash().equals(password)) {
            System.out.println("Authentication successful! Generating token...");
            String token = jwtService.generateToken(login);
            System.out.println("Token generated: " + token);
            return token;
        }

        System.out.println("Authentication failed: password mismatch");
        return null;
    }
}
