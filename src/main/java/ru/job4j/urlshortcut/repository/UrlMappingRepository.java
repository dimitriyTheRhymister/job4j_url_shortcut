package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.entity.Site;
import ru.job4j.urlshortcut.entity.UrlMapping;

import java.util.List;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    List<UrlMapping> findBySite(Site site);

    Optional<UrlMapping> findBySiteAndOriginalUrl(Site site, String originalUrl);

    @Modifying
    @Transactional
    @Query("UPDATE UrlMapping u SET u.clicks = u.clicks + 1 WHERE u.shortCode = :shortCode")
    void incrementClicks(@Param("shortCode") String shortCode);
}