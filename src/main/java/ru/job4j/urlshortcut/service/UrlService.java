package ru.job4j.urlshortcut.service;

import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.dto.StatisticResponse;
import ru.job4j.urlshortcut.entity.Site;
import ru.job4j.urlshortcut.entity.UrlMapping;
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

    public UrlService(UrlMappingRepository urlMappingRepository,
                      SiteRepository siteRepository,
                      CodeGenerator codeGenerator) {
        this.urlMappingRepository = urlMappingRepository;
        this.siteRepository = siteRepository;
        this.codeGenerator = codeGenerator;
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

    public Optional<String> getOriginalUrl(String shortCode) {
        Optional<UrlMapping> mapping = urlMappingRepository.findByShortCode(shortCode);
        mapping.ifPresent(urlMapping ->
                urlMappingRepository.incrementClicks(shortCode)
        );
        return mapping.map(UrlMapping::getOriginalUrl);
    }

    public List<StatisticResponse> getStatistics(String login) {
        Site site = siteRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        // Используем обычный метод findBySite, без DTO в запросе
        List<UrlMapping> mappings = urlMappingRepository.findBySite(site);

        // Преобразуем в DTO в Java
        return mappings.stream()
                .map(m -> new StatisticResponse(m.getOriginalUrl(), m.getClicks()))
                .collect(Collectors.toList());
    }
}