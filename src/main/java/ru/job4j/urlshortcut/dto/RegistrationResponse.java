package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationResponse {
    @JsonProperty("registration")
    private boolean registration;

    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    public RegistrationResponse(boolean registration, String login, String password) {
        this.registration = registration;
        this.login = login;
        this.password = password;
    }

    public boolean isRegistration() {
        return registration;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}