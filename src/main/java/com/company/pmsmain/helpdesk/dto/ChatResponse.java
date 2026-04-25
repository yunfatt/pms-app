package com.company.pmsmain.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {

    private String reply;

    @JsonProperty("queue_id")
    private String queueId;

    @JsonProperty("ticket_id")
    private Long ticketId;

    private String status;
    private String error;

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public String getQueueId() { return queueId; }
    public void setQueueId(String queueId) { this.queueId = queueId; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
