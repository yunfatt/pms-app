package com.company.pmsmain.helpdesk.service;

import com.company.pmsmain.helpdesk.dto.ChatMessage;
import com.company.pmsmain.helpdesk.dto.HelpdeskTicket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("helpdesk_HelpdeskRestService")
public class HelpdeskRestService {

    private static final Logger log = LoggerFactory.getLogger(HelpdeskRestService.class);

    private final RestTemplate rest;
    private final ObjectMapper objectMapper;
    private final String chatBaseUrl;
    private final String supportBaseUrl;

    public HelpdeskRestService(
            @Qualifier("helpdeskRestTemplate") RestTemplate rest,
            ObjectMapper objectMapper,
            @Value("${helpdesk.chat.base-url}") String chatBaseUrl,
            @Value("${helpdesk.support.base-url}") String supportBaseUrl) {
        this.rest = rest;
        this.objectMapper = objectMapper;
        this.chatBaseUrl = chatBaseUrl;
        this.supportBaseUrl = supportBaseUrl;
    }

    public List<HelpdeskTicket> listTickets() {
        try {
            ResponseEntity<List<HelpdeskTicket>> response = rest.exchange(
                    supportBaseUrl + "/api/tickets",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (Exception e) {
            log.error("listTickets failed", e);
            return Collections.emptyList();
        }
    }

    public List<ChatMessage> getMessages(String ticketId) {
        try {
            ResponseEntity<List<ChatMessage>> response = rest.exchange(
                    chatBaseUrl + "/api/messages/" + ticketId,
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

    public boolean sendReply(String ticketId, String message) {
        String url = supportBaseUrl + "/api/tickets/" + ticketId + "/reply";
        try {
            Map<String, String> body = Map.of("message", message);
            ResponseEntity<String> resp = postJson(url, body);
            return resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("sendReply failed for ticketId={}", ticketId, e);
            return false;
        }
    }

    public String sendChatMessage(String userId, String ticketId, String message) {
        String url = chatBaseUrl + "/api/chat";
        try {
            Map<String, String> body = Map.of(
                    "user_id", userId,
                    "ticket_id", ticketId,
                    "message", message);
            ResponseEntity<String> resp = postJson(url, body);
            if (resp == null || !resp.getStatusCode().is2xxSuccessful()) return null;
            String responseBody = resp.getBody();
            if (responseBody == null || responseBody.isBlank()) return null;
            Map<?, ?> json = objectMapper.readValue(responseBody, Map.class);
            Object reply = json.get("reply");
            return reply instanceof String s ? s : reply != null ? reply.toString() : null;
        } catch (Exception e) {
            log.error("sendChatMessage failed for userId={}, ticketId={}", userId, ticketId, e);
            return null;
        }
    }

    public String[] pmsLogin(String pmsUserId, String companyCode) {
        String url = chatBaseUrl + "/api/pms-login";
        try {
            Map<String, String> body = Map.of(
                    "pms_user_id", pmsUserId,
                    "company_code", companyCode);
            ResponseEntity<String> resp = postJson(url, body);
            if (resp == null || !resp.getStatusCode().is2xxSuccessful()) return null;
            String responseBody = resp.getBody();
            if (responseBody == null || responseBody.isBlank()) return null;
            Map<?, ?> json = objectMapper.readValue(responseBody, Map.class);
            Object ticketId = json.get("ticket_id");
            Object userId   = json.get("hd_user_id");
            if (ticketId == null || userId == null) return null;
            return new String[]{
                ticketId instanceof String s ? s : ticketId.toString(),
                userId   instanceof String s ? s : userId.toString()
            };
        } catch (Exception e) {
            log.error("pmsLogin failed for pmsUserId={}, companyCode={}", pmsUserId, companyCode, e);
            return null;
        }
    }

    public String createTicket(String userId) {
        String url = chatBaseUrl + "/api/tickets";
        try {
            Map<String, String> body = Map.of("user_id", userId);
            ResponseEntity<String> resp = postJson(url, body);
            if (resp == null || !resp.getStatusCode().is2xxSuccessful()) return null;
            String responseBody = resp.getBody();
            if (responseBody == null || responseBody.isBlank()) return null;
            Map<?, ?> json = objectMapper.readValue(responseBody, Map.class);
            Object ticketId = json.get("ticket_id");
            return ticketId instanceof String s ? s : ticketId != null ? ticketId.toString() : null;
        } catch (Exception e) {
            log.error("createTicket failed for userId={}", userId, e);
            return null;
        }
    }

    private ResponseEntity<String> postJson(String url, Object bodyObj) {
        try {
            String json = objectMapper.writeValueAsString(bodyObj);
            log.debug("POST {} body={}", url, json);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<String> request = new HttpEntity<>(json, headers);
            return rest.exchange(url, HttpMethod.POST, request, String.class);
        } catch (HttpClientErrorException e) {
            log.error("HTTP {} on POST {}: {}", e.getStatusCode(), url, e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            log.error("POST {} failed: {}", url, e.getMessage());
            return null;
        }
    }
}
