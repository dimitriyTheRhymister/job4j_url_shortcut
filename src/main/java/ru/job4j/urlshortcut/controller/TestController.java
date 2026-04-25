package ru.job4j.urlshortcut.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, String> test(@RequestAttribute("login") String login) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Authorized successfully!");
        response.put("login", login);
        return response;
    }
}