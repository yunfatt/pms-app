package com.company.pmsmain.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {

    private String message;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("ticket_id")
    private Long ticketId;

    public ChatRequest() {}

    public ChatRequest(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }

    public ChatRequest(String message, String userId, Long ticketId) {
        this.message = message;
        this.userId = userId;
        this.ticketId = ticketId;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
}
