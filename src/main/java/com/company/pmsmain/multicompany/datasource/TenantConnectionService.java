package com.company.pmsmain.multicompany.datasource;

import com.company.pmsmain.multicompany.service.TenantDebugService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TenantConnectionService {

    private final TenantDebugService tenantDebugService;
    private final Map<String, String> tenantConnections;

    public TenantConnectionService(
            TenantDebugService tenantDebugService,
            @Value("#{${app.tenant.connections:{}}}") Map<String, String> tenantConnections
    ) {
        this.tenantDebugService = tenantDebugService;
        this.tenantConnections = tenantConnections;
    }

    public String getCurrentTenantConnection() {
        String companyCode = tenantDebugService.getCurrentCompanyCode();

        if (companyCode == null || companyCode.isBlank()) {
            throw new IllegalStateException("No current tenant/company selected.");
        }

        String connection = tenantConnections.get(companyCode);
        if (connection == null || connection.isBlank()) {
            throw new IllegalStateException(
                    "No report DB connection configured for tenant: " + companyCode
            );
        }

        return connection;
    }
}