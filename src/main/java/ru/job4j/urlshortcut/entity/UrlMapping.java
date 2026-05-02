package ru.job4j.urlshortcut.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "url_mappings")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalUrl;

    @Column(unique = true, nullable = false)
    private String shortCode;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long clicks = 0L;

    // Конструктор для создания новой ссылки
    public UrlMapping(String originalUrl, String shortCode, Site site) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.site = site;
        this.createdAt = LocalDateTime.now();
        this.clicks = 0L;
    }
}