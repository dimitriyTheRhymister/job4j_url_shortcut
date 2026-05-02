package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequest {
    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;
}