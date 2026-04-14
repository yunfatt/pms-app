package com.company.pmsmain.multicompany.config;

import com.company.pmsmain.multicompany.datasource.CompanyRoutingDataSource;
import com.company.pmsmain.multicompany.service.CompanyDataSourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class CompanyRoutingDataSourceConfig {

    @Bean(name = "tenantDataSource")
    public DataSource dataSource(CompanyDataSourceService service) {
        CompanyRoutingDataSource routing = new CompanyRoutingDataSource();
        Map<Object, Object> targetDataSources = service.buildTargetDataSources();

        if (targetDataSources == null || targetDataSources.isEmpty()) {
            throw new IllegalStateException("No tenant data sources configured from APP_COMPANY");
        }

        // Log registered keys
        System.out.println(">>> Registered datasource keys: " + targetDataSources.keySet());

        routing.setTargetDataSources(targetDataSources);
        routing.setDefaultTargetDataSource(targetDataSources.values().iterator().next());
        routing.afterPropertiesSet();
        return routing;
    }
}