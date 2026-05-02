package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {
    @JsonProperty("registration")
    private boolean registration;

    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;
}