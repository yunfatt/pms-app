package com.company.pmsmain.helpdesk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateTicketRequest {

    private String subject;
    private String description;
    private String priority = "medium";

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("user_id")
    private String userId;

    public CreateTicketRequest() {}

    public CreateTicketRequest(String subject, String description, String priority,
                               String customerId, String customerName, String userId) {
        this.subject = subject;
        this.description = description;
        this.priority = priority;
        this.customerId = customerId;
        this.customerName = customerName;
        this.userId = userId;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
