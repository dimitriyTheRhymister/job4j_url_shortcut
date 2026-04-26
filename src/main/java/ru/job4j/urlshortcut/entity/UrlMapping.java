package ru.job4j.urlshortcut.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    public UrlMapping() {

    }

    public UrlMapping(String originalUrl, String shortCode, Site site) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.site = site;
        this.createdAt = LocalDateTime.now();
        this.clicks = 0L;
    }

    public Long getId() {
        return id; }

    public void setId(Long id) {
        this.id = id; }

    public String getOriginalUrl() {
        return originalUrl; }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl; }

    public String getShortCode() {
        return shortCode; }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode; }

    public Site getSite() {
        return site; }

    public void setSite(Site site) {
        this.site = site; }

    public LocalDateTime getCreatedAt() {
        return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt; }

    public Long getClicks() {
        return clicks; }

    public void setClicks(Long clicks) {
        this.clicks = clicks; }
}