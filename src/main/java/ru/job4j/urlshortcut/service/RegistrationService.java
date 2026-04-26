package ru.job4j.urlshortcut.service;

import ru.job4j.urlshortcut.dto.RegistrationResponse;
import ru.job4j.urlshortcut.entity.Site;
import ru.job4j.urlshortcut.generator.CredentialGenerator;
import ru.job4j.urlshortcut.repository.SiteRepository;
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
        if (siteRepository.existsBySiteName(siteName)) {
            return new RegistrationResponse(false, null, null);
        }
        
        String login = credentialGenerator.generateLogin();
        String password = credentialGenerator.generatePassword();
        
        Site site = new Site(siteName, login, password);
        siteRepository.save(site);
        
        return new RegistrationResponse(true, login, password);
    }
}