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
@Table(name = "sites")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String siteName;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String passwordHash;

    private LocalDateTime createdAt;

    // Конструктор для создания нового сайта (без id)
    public Site(String siteName, String login, String passwordHash) {
        this.siteName = siteName;
        this.login = login;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }
}