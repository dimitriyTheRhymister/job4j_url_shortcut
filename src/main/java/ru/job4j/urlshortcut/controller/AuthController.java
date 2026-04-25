package ru.job4j.urlshortcut.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.dto.AuthRequest;
import ru.job4j.urlshortcut.dto.AuthResponse;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;
    private final SiteRepository siteRepository;

    public AuthController(AuthService authService, SiteRepository siteRepository) {
        this.authService = authService;
        this.siteRepository = siteRepository;
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        String token = authService.authenticate(request.getLogin(), request.getPassword());

        if (token != null) {
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/auth-test")
    public ResponseEntity<AuthResponse> authenticateTest(
            @RequestParam String login,
            @RequestParam String password) {
        System.out.println("=== GET AUTH-TEST CALLED ===");
        System.out.println("Login: " + login);
        System.out.println("Password: " + password);

        String token = authService.authenticate(login, password);

        if (token != null) {
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "AuthController is alive!";
    }

    @GetMapping("/check-login")
    public String checkLogin(@RequestParam String login) {
        var site = siteRepository.findByLogin(login);
        if (site.isPresent()) {
            return "Found: " + site.get().getSiteName() + ", login: " + site.get().getLogin();
        } else {
            return "Not found for login: " + login;
        }
    }
}
