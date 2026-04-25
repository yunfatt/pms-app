package com.company.pmsmain.helpdesk.client;

import com.company.pmsmain.helpdesk.dto.MessageDto;
import com.company.pmsmain.helpdesk.dto.ReplyRequest;
import com.company.pmsmain.helpdesk.dto.TicketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Component
public class SupportApiClient {

    private static final Logger log = LoggerFactory.getLogger(SupportApiClient.class);

    private final RestTemplate rest;

    public SupportApiClient(@Qualifier("supportApiRestTemplate") RestTemplate rest) {
        this.rest = rest;
    }

    public List<TicketDto> getTickets() {
        try {
            ResponseEntity<List<TicketDto>> response = rest.exchange(
                    "/api/tickets",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("getTickets failed", e);
            return Collections.emptyList();
        }
    }

    public MessageDto replyToTicket(Long ticketId, ReplyRequest request) {
        try {
            return rest.postForObject(
                    "/api/tickets/" + ticketId + "/reply",
                    request,
                    MessageDto.class
            );
        } catch (Exception e) {
            log.error("replyToTicket failed for ticketId={}", ticketId, e);
            return null;
        }
    }
}
