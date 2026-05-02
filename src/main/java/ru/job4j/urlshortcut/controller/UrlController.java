package ru.job4j.urlshortcut.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.dto.ConvertRequest;
import ru.job4j.urlshortcut.dto.ConvertResponse;
import ru.job4j.urlshortcut.dto.StatisticResponse;
import ru.job4j.urlshortcut.service.UrlService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/convert")
    public ResponseEntity<ConvertResponse> convert(@RequestBody ConvertRequest request,
                                                   @RequestAttribute("login") String login) {
        String shortCode = urlService.convertUrl(request.getUrl(), login);
        return ResponseEntity.ok(new ConvertResponse(shortCode));
    }

    @GetMapping("/redirect/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getOriginalUrl(code);
        response.sendRedirect(originalUrl);
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<StatisticResponse>> getStatistics(@RequestAttribute("login") String login) {
        return ResponseEntity.ok(urlService.getStatistics(login));
    }
}