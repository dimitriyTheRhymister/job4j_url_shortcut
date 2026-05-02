package ru.job4j.urlshortcut.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.dto.StatisticResponse;
import ru.job4j.urlshortcut.entity.Site;
import ru.job4j.urlshortcut.entity.UrlMapping;
import ru.job4j.urlshortcut.generator.CodeGenerator;
import ru.job4j.urlshortcut.repository.SiteRepository;
import ru.job4j.urlshortcut.repository.UrlMappingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UrlService {

    private final UrlMappingRepository urlMappingRepository;
    private final SiteRepository siteRepository;
    private final CodeGenerator codeGenerator;
    private final String databaseType;

    public UrlService(UrlMappingRepository urlMappingRepository,
                      SiteRepository siteRepository,
                      CodeGenerator codeGenerator,
                      @Value("${database.type:h2}") String databaseType) {
        this.urlMappingRepository = urlMappingRepository;
        this.siteRepository = siteRepository;
        this.codeGenerator = codeGenerator;
        this.databaseType = databaseType;
    }

    public String convertUrl(String originalUrl, String login) {
        Optional<Site> siteOpt = siteRepository.findByLogin(login);
        if (siteOpt.isEmpty()) {
            throw new RuntimeException("Site not found");
        }

        Site site = siteOpt.get();

        Optional<UrlMapping> existingMapping = urlMappingRepository.findBySiteAndOriginalUrl(site, originalUrl);
        if (existingMapping.isPresent()) {
            return existingMapping.get().getShortCode();
        }

        String shortCode;
        do {
            shortCode = codeGenerator.generateCode();
        } while (urlMappingRepository.findByShortCode(shortCode).isPresent());

        UrlMapping urlMapping = new UrlMapping(originalUrl, shortCode, site);
        urlMappingRepository.save(urlMapping);

        return shortCode;
    }

    @Transactional
    public String getOriginalUrl(String shortCode) {
        /* Для PostgreSQL — один запрос с RETURNING */
        if ("postgres".equalsIgnoreCase(databaseType)) {
            return urlMappingRepository.incrementAndGetUrl(shortCode)
                    .orElseThrow(() -> new RuntimeException("URL not found"));
        }

        /* Для H2 (тесты, разработка) — два запроса в транзакции */
        UrlMapping mapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));
        urlMappingRepository.incrementClicks(shortCode);
        return mapping.getOriginalUrl();
    }

    public List<StatisticResponse> getStatistics(String login) {
        Site site = siteRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        List<UrlMapping> mappings = urlMappingRepository.findBySite(site);

        return mappings.stream()
                .map(m -> new StatisticResponse(m.getOriginalUrl(), m.getClicks()))
                .collect(Collectors.toList());
    }
}