package com.company.pmsmain.helpdesk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.Duration;

@Configuration
public class HelpdeskConfig {

    @Value("${helpdesk.chat-api.base-url:http://192.168.0.147:7863}")
    private String chatApiUrl;

    @Value("${helpdesk.support-api.base-url:http://192.168.0.147:7870}")
    private String supportApiUrl;

    @Value("${helpdesk.admin-api.base-url:http://192.168.0.147:7865}")
    private String adminApiUrl;

    @Bean("chatApiRestTemplate")
    public RestTemplate chatApiRestTemplate() {
        return buildTemplate(chatApiUrl, Duration.ofSeconds(10), Duration.ofSeconds(60));
    }

    @Bean("supportApiRestTemplate")
    public RestTemplate supportApiRestTemplate() {
        return buildTemplate(supportApiUrl, Duration.ofSeconds(10), Duration.ofSeconds(30));
    }

    @Bean("adminApiRestTemplate")
    public RestTemplate adminApiRestTemplate() {
        return buildTemplate(adminApiUrl, Duration.ofSeconds(10), Duration.ofSeconds(30));
    }

    private RestTemplate buildTemplate(String rootUrl, Duration connectTimeout, Duration readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        RestTemplate rt = new RestTemplate(factory);
        rt.setUriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
        return rt;
    }
}
