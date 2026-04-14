package com.company.pmsmain.multicompany.service;

import com.company.pmsmain.multicompany.context.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class CompanyRoutingTestService {

    @Autowired
    private DataSource companyRoutingDataSource;

    public String testConnection() {
        String tenant = TenantContext.getCompanyCode();

        if (tenant == null || tenant.isBlank()) {
            return "No tenant in context";
        }

        try (Connection connection = companyRoutingDataSource.getConnection()) {
            return "Connected to tenant DB for: " + tenant +
                    ", catalog=" + connection.getCatalog();
        } catch (Exception e) {
            return "Connection failed for tenant " + tenant + ": " + e.getMessage();
        }
    }
}