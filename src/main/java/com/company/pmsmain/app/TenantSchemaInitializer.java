package com.company.pmsmain.tenant;

import com.company.pmsmain.entity.AppCompany;
import io.jmix.core.UnconstrainedDataManager;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class TenantSchemaInitializer implements ApplicationRunner, ResourceLoaderAware {

    private static final Logger log = LoggerFactory.getLogger(TenantSchemaInitializer.class);

    private final UnconstrainedDataManager dataManager;
    private ResourceLoader resourceLoader;

    @Value("${tenant.liquibase.change-log}")
    private String changeLog;

    public TenantSchemaInitializer(UnconstrainedDataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>> TenantSchemaInitializer starting — changeLog: {}", changeLog);

        List<AppCompany> companies = dataManager.load(AppCompany.class)
                .query("e.active = true")
                .list();

        log.info(">>> Found {} active companies", companies.size());

        for (AppCompany company : companies) {
            log.info("Running Liquibase schema init for: {}", company.getCompanyCode());
            try {
                DataSource ds = DataSourceBuilder.create()
                        .driverClassName("org.postgresql.Driver")
                        .url("jdbc:postgresql://" + company.getDbHost() + ":"
                                + company.getDbPort() + "/" + company.getDbName())
                        .username(company.getDbUsername())
                        .password(company.getDbPasswordEnc())
                        .build();

                SpringLiquibase liquibase = new SpringLiquibase();
                liquibase.setDataSource(ds);
                liquibase.setChangeLog(changeLog);
                liquibase.setResourceLoader(resourceLoader); // ← fix for NullPointerException
                liquibase.setShouldRun(true);
                liquibase.afterPropertiesSet();

                log.info("Liquibase completed for: {}", company.getCompanyCode());
            } catch (Exception e) {
                log.error("Liquibase failed for: {} — {}", company.getCompanyCode(), e.getMessage());
            }
        }
    }
}
