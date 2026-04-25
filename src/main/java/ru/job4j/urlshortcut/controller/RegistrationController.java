package ru.job4j.urlshortcut.controller;

import ru.job4j.urlshortcut.dto.RegistrationRequest;
import ru.job4j.urlshortcut.dto.RegistrationResponse;
import ru.job4j.urlshortcut.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {
    
    private final RegistrationService registrationService;
    
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    
    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        RegistrationResponse response = registrationService.registerSite(request.getSite());
        return ResponseEntity.ok(response);
    }
}