package com.company.pmsmain.tenant;

import io.jmix.core.JmixModules;
import io.jmix.core.Resources;
import io.jmix.data.impl.JmixEntityManagerFactoryBean;
import io.jmix.data.impl.JmixTransactionManager;
import io.jmix.data.persistence.DbmsSpecifics;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TenantStoreConfiguration {

    @Bean("tenantDataSourceProperties")
    @ConfigurationProperties("tenant.datasource")
    public DataSourceProperties tenantDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            @Qualifier("tenantDataSource") DataSource dataSource,
            JpaVendorAdapter jpaVendorAdapter,
            DbmsSpecifics dbmsSpecifics,
            JmixModules jmixModules,
            Resources resources
    ) {
        JmixEntityManagerFactoryBean factoryBean = new JmixEntityManagerFactoryBean(
                "tenant",
                dataSource,
                jpaVendorAdapter,
                dbmsSpecifics,
                jmixModules,
                resources
        );

        Map<String, Object> jpaProps = new HashMap<>();

        // Force EclipseLink to always fetch connections from CompanyRoutingDataSource
        jpaProps.put("eclipselink.jdbc.exclusive-connection.mode", "Always");
        jpaProps.put("eclipselink.connection-pool.default.initial", "0");
        jpaProps.put("eclipselink.connection-pool.default.min", "0");
        jpaProps.put("eclipselink.connection-pool.default.max", "1");

        // Disable L2 cache to prevent stale cross-tenant data
        jpaProps.put("eclipselink.cache.shared.default", "false");
        jpaProps.put("eclipselink.cache.size.default", "0");
        jpaProps.put("eclipselink.cache.type.default", "NONE");

        factoryBean.setJpaPropertyMap(jpaProps);

        return factoryBean;
    }

    @Bean("tenantTransactionManager")
    public JpaTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory emf
    ) {
        return new JmixTransactionManager("tenant", emf);
    }
}
