package com.company.pmsmain.multicompany.service;

import com.company.pmsmain.entity.AppCompany;
import io.jmix.core.UnconstrainedDataManager;
import org.springframework.stereotype.Service;

@Service
public class CompanyRegistryService {

    private final UnconstrainedDataManager dataManager; // ← change here

    public CompanyRegistryService(UnconstrainedDataManager dataManager) { // ← change here
        this.dataManager = dataManager;
    }

    public AppCompany findBySubdomain(String subdomain) {
        if (subdomain == null || subdomain.isBlank()) return null;

        return dataManager.load(AppCompany.class)
                .query("e.subdomain = ?1 and e.active = true", subdomain.toLowerCase())
                .optional()
                .orElse(null);
    }

    public AppCompany findByPathKey(String pathKey) {
        if (pathKey == null || pathKey.isBlank()) return null;

        return dataManager.load(AppCompany.class)
                .query("e.pathKey = ?1 and e.active = true", pathKey.toLowerCase())
                .optional()
                .orElse(null);
    }

    public AppCompany findByCompanyCode(String companyCode) {
        if (companyCode == null || companyCode.isBlank()) return null;

        String normalised = companyCode.trim().toUpperCase();
        System.out.println(">>> findByCompanyCode normalised: [" + normalised + "]");

        return dataManager.load(AppCompany.class)
                .query("e.companyCode = ?1 and e.active = true", normalised)
                .optional()
                .orElse(null);
    }
}