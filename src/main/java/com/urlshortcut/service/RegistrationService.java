package com.urlshortcut.service;

import com.urlshortcut.dto.RegistrationResponse;
import com.urlshortcut.entity.Site;
import com.urlshortcut.repository.SiteRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    
    private final SiteRepository siteRepository;
    private final CredentialGenerator credentialGenerator;
    
    public RegistrationService(SiteRepository siteRepository, 
                               CredentialGenerator credentialGenerator) {
        this.siteRepository = siteRepository;
        this.credentialGenerator = credentialGenerator;
    }
    
    public RegistrationResponse registerSite(String siteName) {
        // Проверяем, существует ли уже такой сайт
        if (siteRepository.existsBySiteName(siteName)) {
            return new RegistrationResponse(false, null, null);
        }
        
        // Генерируем уникальные логин и пароль
        String login = credentialGenerator.generateLogin();
        String password = credentialGenerator.generatePassword();
        
        // Сохраняем сайт в БД (в реальном проекте пароль нужно хешировать!)
        Site site = new Site(siteName, login, password);
        siteRepository.save(site);
        
        return new RegistrationResponse(true, login, password);
    }
}