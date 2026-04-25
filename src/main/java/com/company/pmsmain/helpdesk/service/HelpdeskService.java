package com.company.pmsmain.helpdesk.service;

import com.company.pmsmain.helpdesk.client.AdminApiClient;
import com.company.pmsmain.helpdesk.client.ChatApiClient;
import com.company.pmsmain.helpdesk.client.SupportApiClient;
import com.company.pmsmain.helpdesk.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpdeskService {

    private final ChatApiClient chatApiClient;
    private final SupportApiClient supportApiClient;
    private final AdminApiClient adminApiClient;

    public HelpdeskService(ChatApiClient chatApiClient,
                           SupportApiClient supportApiClient,
                           AdminApiClient adminApiClient) {
        this.chatApiClient = chatApiClient;
        this.supportApiClient = supportApiClient;
        this.adminApiClient = adminApiClient;
    }

    // ── Ticket operations ─────────────────────────────────────────────────────

    public List<TicketDto> getTickets() {
        return supportApiClient.getTickets();
    }

    public TicketDto createTicket(CreateTicketRequest request) {
        return chatApiClient.createTicket(request);
    }

    public MessageDto replyToTicket(Long ticketId, ReplyRequest request) {
        return supportApiClient.replyToTicket(ticketId, request);
    }

    // ── Message operations ────────────────────────────────────────────────────

    public List<MessageDto> getMessages(Long ticketId) {
        return chatApiClient.getMessages(ticketId);
    }

    // ── Chat operations ───────────────────────────────────────────────────────

    public ChatResponse sendChat(ChatRequest request) {
        return chatApiClient.sendChat(request);
    }

    // ── Progress tracking ─────────────────────────────────────────────────────

    public ProgressDto getProgress(String queueId) {
        return adminApiClient.getProgress(queueId);
    }
}
