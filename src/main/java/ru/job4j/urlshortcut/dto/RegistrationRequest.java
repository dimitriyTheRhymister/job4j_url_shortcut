package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationRequest {
    @JsonProperty("site")
    private String site;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}