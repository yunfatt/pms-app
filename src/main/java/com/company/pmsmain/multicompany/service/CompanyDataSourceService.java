package com.company.pmsmain.multicompany.service;

import com.company.pmsmain.entity.AppCompany;
import io.jmix.core.UnconstrainedDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyDataSourceService {

    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    public Map<Object, Object> buildTargetDataSources() {
        List<AppCompany> companies = unconstrainedDataManager.load(AppCompany.class)
                .all()
                .list();

        Map<Object, Object> dataSources = new HashMap<>();

        for (AppCompany company : companies) {
            DataSource ds = DataSourceBuilder.create()
                    .driverClassName("org.postgresql.Driver")
                    .url("jdbc:postgresql://" + company.getDbHost() + ":"
                            + company.getDbPort() + "/" + company.getDbName())
                    .username(company.getDbUsername())
                    .password(company.getDbPasswordEnc())
                    .build();

            String key = company.getCompanyCode().trim().toUpperCase();
            System.out.println(">>> Registered datasource key: [" + key + "] → " + company.getDbName());
            dataSources.put(key, ds);
        }

        return dataSources;
    }
}
