package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConvertRequest {
    @JsonProperty("url")
    private String url;
}