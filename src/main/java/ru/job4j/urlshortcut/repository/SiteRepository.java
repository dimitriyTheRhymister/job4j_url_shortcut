package ru.job4j.urlshortcut.repository;

import ru.job4j.urlshortcut.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {
    Optional<Site> findBySiteName(String siteName);
    Optional<Site> findByLogin(String login);
    boolean existsBySiteName(String siteName);
}