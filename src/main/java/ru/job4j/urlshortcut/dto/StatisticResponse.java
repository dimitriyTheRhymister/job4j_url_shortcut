package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticResponse {
    @JsonProperty("url")
    private String url;

    @JsonProperty("total")
    private Long total;
}