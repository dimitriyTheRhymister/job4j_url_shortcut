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
import java.util.stream.Collectors;

@Service
public class UrlService {

    /**
     * Максимальное количество попыток генерации уникального кода.
     * Это защита от бесконечного цикла на случай, если пространство кодов исчерпано
     * или произошла ошибка при проверке уникальности.
     * При 10 попытках и 3.5 триллионах возможных кодов вероятность коллизии ~ 0%.
     */
    private static final int MAX_CODE_ATTEMPTS = 10;

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

    /**
     * Преобразует длинную ссылку в короткий код.
     * Алгоритм:
     * 1. Находим сайт по логину из JWT токена. Если не найден — исключение.
     * 2. Проверяем, не сокращали ли мы уже этот URL для данного сайта.
     *    Если да — возвращаем существующий код (детерминизм).
     * 3. Если нет — генерируем и сохраняем новый уникальный код.
     *
     * @param originalUrl исходная длинная ссылка
     * @param login логин сайта из JWT токена
     * @return уникальный короткий код (например, "Vy4TZ8C")
     */
    @Transactional
    public String convertUrl(String originalUrl, String login) {
        /* 1. Находим сайт или падаем сразу с понятным исключением */
        Site site = siteRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Site not found: " + login));

        /* 2. Если ссылка уже укорочена — возвращаем существующий код
           map() — если найден, берём shortCode
           orElseGet() — если не найден, генерируем новый */
        return urlMappingRepository.findBySiteAndOriginalUrl(site, originalUrl)
                .map(UrlMapping::getShortCode)
                .orElseGet(() -> generateAndSaveUniqueCode(originalUrl, site));
    }

    /**
     * Генерирует уникальный короткий код и сохраняет его в БД.
     * Алгоритм:
     * 1. Пытаемся сгенерировать код через SecureRandom.
     * 2. Проверяем, существует ли уже такой код в БД.
     * 3. Если существует — повторяем попытку (максимум MAX_CODE_ATTEMPTS раз).
     * 4. Если удалось найти уникальный — сохраняем и возвращаем.
     * 5. Если после всех попыток не удалось — бросаем исключение.
     * Защита от бесконечного цикла осуществляется через MAX_CODE_ATTEMPTS.
     * Это важно, так как в теории (при 3.5 трлн ссылок) можно попасть в коллизию.
     * На практике 10 попыток — это перестраховка, хватило бы и 2-3.
     *
     * @param originalUrl исходная ссылка
     * @param site владелец ссылки
     * @return уникальный короткий код
     */
    private String generateAndSaveUniqueCode(String originalUrl, Site site) {
        /* Защита от бесконечного цикла: for с лимитом */
        for (int attempt = 0; attempt < MAX_CODE_ATTEMPTS; attempt++) {
            String code = codeGenerator.generateCode();

            /* Проверяем уникальность через exists */
            if (!urlMappingRepository.existsByShortCode(code)) {
                UrlMapping mapping = new UrlMapping(originalUrl, code, site);
                urlMappingRepository.save(mapping);
                return code;
            }
        }

        /* Если после всех попыток не повезло — явная ошибка
           вместо зависания в бесконечном цикле */
        throw new RuntimeException(
                "Failed to generate unique short code after " + MAX_CODE_ATTEMPTS + " attempts"
        );
    }

    /**
     * Получает оригинальный URL по короткому коду и увеличивает счётчик кликов.
     * Работает по-разному в зависимости от типа БД:
     * - PostgreSQL: один запрос с RETURNING (атомарно)
     * - H2: два запроса в транзакции (H2 не поддерживает RETURNING)
     *
     * @param shortCode короткий код
     * @return оригинальный URL
     */
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

    /**
     * Возвращает статистику по всем ссылкам сайта.
     *
     * @param login логин сайта из JWT токена
     * @return список объектов с url и количеством переходов
     */
    public List<StatisticResponse> getStatistics(String login) {
        Site site = siteRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        List<UrlMapping> mappings = urlMappingRepository.findBySite(site);

        return mappings.stream()
                .map(m -> new StatisticResponse(m.getOriginalUrl(), m.getClicks()))
                .collect(Collectors.toList());
    }
}