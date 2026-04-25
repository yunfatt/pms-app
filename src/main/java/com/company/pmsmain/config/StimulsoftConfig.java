package com.company.pmsmain.config;

import com.stimulsoft.base.licenses.StiLicense;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StimulsoftConfig {

    @Value("${stimulsoft.license-key:}")
    private String licenseKey;

    @Bean
    public StiLicense stimulsoftLicense() {
        if (licenseKey != null && !licenseKey.isBlank()) {
            StiLicense.setKey(licenseKey);
        }
        return new StiLicense();
    }
}
