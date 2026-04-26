package ru.job4j.urlshortcut.generator;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class CredentialGenerator {
    
    public String generateLogin() {
        /* login = "site_" + короткий уникальный код */
        return "site_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public String generatePassword() {
        /* пароль = UUID (32 символа + дефисы) */
        return UUID.randomUUID().toString();
    }
}