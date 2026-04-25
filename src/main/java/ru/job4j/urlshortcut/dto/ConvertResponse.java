package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConvertResponse {
    @JsonProperty("code")
    private String code;

    public ConvertResponse(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
