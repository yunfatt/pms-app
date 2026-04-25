package com.company.pmsmain.helpdesk.client;

import com.company.pmsmain.helpdesk.dto.ChatRequest;
import com.company.pmsmain.helpdesk.dto.ChatResponse;
import com.company.pmsmain.helpdesk.dto.CreateTicketRequest;
import com.company.pmsmain.helpdesk.dto.MessageDto;
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
public class ChatApiClient {

    private static final Logger log = LoggerFactory.getLogger(ChatApiClient.class);

    private final RestTemplate rest;

    public ChatApiClient(@Qualifier("chatApiRestTemplate") RestTemplate rest) {
        this.rest = rest;
    }

    public List<MessageDto> getMessages(Long ticketId) {
        try {
            ResponseEntity<List<MessageDto>> response = rest.exchange(
                    "/api/messages/" + ticketId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("getMessages failed for ticketId={}", ticketId, e);
            return Collections.emptyList();
        }
    }

    public ChatResponse sendChat(ChatRequest request) {
        try {
            return rest.postForObject("/api/chat", request, ChatResponse.class);
        } catch (Exception e) {
            log.error("sendChat failed", e);
            return null;
        }
    }

    public TicketDto createTicket(CreateTicketRequest request) {
        try {
            return rest.postForObject("/api/tickets", request, TicketDto.class);
        } catch (Exception e) {
            log.error("createTicket failed for userId={}", request != null ? request.getUserId() : null, e);
            return null;
        }
    }
}
