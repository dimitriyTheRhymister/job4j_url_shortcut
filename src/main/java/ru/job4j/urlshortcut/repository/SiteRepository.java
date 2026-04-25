package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.urlshortcut.entity.Site;

import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {
    Optional<Site> findBySiteName(String siteName);
    Optional<Site> findByLogin(String login);
    boolean existsBySiteName(String siteName);

    // ⬇️ ДОБАВЛЯЕМ ЭТОТ МЕТОД
    Optional<Site> findByLoginAndPasswordHash(String login, String passwordHash);
}