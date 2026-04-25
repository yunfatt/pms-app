package com.company.pmsmain.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplyRequest {

    private String content;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("sender_type")
    private String senderType = "agent";

    public ReplyRequest() {}

    public ReplyRequest(String content, String userId) {
        this.content = content;
        this.userId = userId;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
}
