package ru.job4j.urlshortcut.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatisticResponse {
    @JsonProperty("url")
    private String url;

    @JsonProperty("total")
    private Long total;

    public StatisticResponse(String url, Long total) {
        this.url = url;
        this.total = total;
    }

    public String getUrl() { return url; }
    public Long getTotal() { return total; }
}
