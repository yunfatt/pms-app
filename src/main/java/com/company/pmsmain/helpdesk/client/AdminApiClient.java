package com.company.pmsmain.helpdesk.client;

import com.company.pmsmain.helpdesk.dto.ProgressDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AdminApiClient {

    private static final Logger log = LoggerFactory.getLogger(AdminApiClient.class);

    private final RestTemplate rest;

    public AdminApiClient(@Qualifier("adminApiRestTemplate") RestTemplate rest) {
        this.rest = rest;
    }

    public ProgressDto getProgress(String queueId) {
        if (queueId == null || queueId.isBlank()) {
            log.warn("getProgress called with null/blank queueId");
            return null;
        }
        try {
            return rest.getForObject("/api/progress/" + queueId, ProgressDto.class);
        } catch (Exception e) {
            log.error("getProgress failed for queueId={}", queueId, e);
            return null;
        }
    }
}
