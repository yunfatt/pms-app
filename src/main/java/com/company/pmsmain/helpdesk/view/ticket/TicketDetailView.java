package com.company.pmsmain.helpdesk.view.ticket;

import com.company.pmsmain.helpdesk.dto.MessageDto;
import com.company.pmsmain.helpdesk.dto.ReplyRequest;
import com.company.pmsmain.helpdesk.dto.TicketDto;
import com.company.pmsmain.helpdesk.service.HelpdeskService;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "helpdesk-ticket/:ticketId", layout = MainView.class)
@ViewController(id = "Helpdesk.ticketDetail")
@ViewDescriptor(path = "ticket-detail-view.xml")
public class TicketDetailView extends StandardView implements BeforeEnterObserver {

    @ViewComponent private Span              ticketTitleLabel;
    @ViewComponent private Span              statusBadge;
    @ViewComponent private Span              priorityBadge;
    @ViewComponent private Span              customerLabel;
    @ViewComponent private Span              assignedLabel;
    @ViewComponent private Span              createdLabel;
    @ViewComponent private Span              updatedLabel;
    @ViewComponent private VerticalLayout    messagesBox;
    @ViewComponent private TextArea          replyArea;

    @Autowired private HelpdeskService       helpdeskService;
    @Autowired private Notifications         notifications;
    @Autowired private CurrentAuthentication currentAuthentication;

    private Long ticketId;
    private TicketDto currentTicket;

    // ── Route parameter extraction ────────────────────────────────────────────

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("ticketId").ifPresent(id -> {
            try { ticketId = Long.parseLong(id); } catch (NumberFormatException ignored) {}
        });
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if (ticketId != null) {
            loadTicketDetail();
        }
    }

    // ── Button handlers ───────────────────────────────────────────────────────

    @Subscribe("backButton")
    public void onBack(ClickEvent<JmixButton> e) {
        getUI().ifPresent(ui -> ui.navigate("helpdesk-tickets"));
    }

    @Subscribe("sendReplyButton")
    public void onSendReply(ClickEvent<JmixButton> e) {
        String text = replyArea.getValue();
        if (text == null || text.isBlank()) {
            notifications.create("Reply cannot be empty")
                    .withType(Notifications.Type.WARNING).show();
            return;
        }
        sendReply(text.trim());
    }

    @Subscribe("aiAssistButton")
    public void onAiAssist(ClickEvent<JmixButton> e) {
        if (currentTicket == null) return;
        String context = "Ticket: " + currentTicket.getSubject()
                + "\nDescription: " + currentTicket.getDescription();
        getUI().ifPresent(ui -> ui.navigate(
                "helpdesk-chat?context=" + java.net.URLEncoder.encode(context, java.nio.charset.StandardCharsets.UTF_8)));
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    private void loadTicketDetail() {
        try {
            List<TicketDto> tickets = helpdeskService.getTickets();
            currentTicket = tickets.stream()
                    .filter(t -> ticketId.equals(t.getId()))
                    .findFirst().orElse(null);

            if (currentTicket == null) {
                ticketTitleLabel.setText("Ticket #" + ticketId + " not found");
                return;
            }

            renderTicketHeader(currentTicket);
            loadMessages();

        } catch (Exception ex) {
            notifications.create("Failed to load ticket: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR).show();
        }
    }

    private void loadMessages() {
        try {
            List<MessageDto> messages = helpdeskService.getMessages(ticketId);
            renderMessages(messages);
        } catch (Exception ex) {
            notifications.create("Failed to load messages: " + ex.getMessage())
                    .withType(Notifications.Type.WARNING).show();
        }
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    private void renderTicketHeader(TicketDto ticket) {
        ticketTitleLabel.setText("#" + ticket.getId() + " — " + ticket.getSubject());

        applyStatusStyle(statusBadge, ticket.getStatus());
        applyPriorityStyle(priorityBadge, ticket.getPriority());

        customerLabel.setText("Customer:  " + nvl(ticket.getCustomerName()));
        assignedLabel.setText("Assigned:  " + nvl(ticket.getAssignedTo()));
        createdLabel.setText("Created:   " + formatDate(ticket.getCreatedAt()));
        updatedLabel.setText("Updated:   " + formatDate(ticket.getUpdatedAt()));
    }

    private void renderMessages(List<MessageDto> messages) {
        messagesBox.removeAll();

        if (messages.isEmpty()) {
            Span empty = new Span("No messages yet.");
            empty.getStyle().set("color", "#9e9e9e").set("font-style", "italic");
            messagesBox.add(empty);
            return;
        }

        for (MessageDto msg : messages) {
            messagesBox.add(buildMessageBubble(msg));
        }

        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "setTimeout(() => { var el = document.querySelector('.helpdesk-messages-scroll'); " +
                "if(el) el.scrollTop = el.scrollHeight; }, 100);"));
    }

    private Div buildMessageBubble(MessageDto msg) {
        boolean isAgent = "agent".equalsIgnoreCase(msg.getSenderType());
        boolean isAi    = "ai".equalsIgnoreCase(msg.getSenderType());

        Div bubble = new Div();
        bubble.getStyle()
                .set("max-width", "75%")
                .set("padding", "10px 14px")
                .set("border-radius", isAgent || isAi ? "12px 12px 4px 12px" : "12px 12px 12px 4px")
                .set("background", isAi ? "#e8f5e9" : isAgent ? "#e3f2fd" : "#fff")
                .set("border", "1px solid " + (isAi ? "#a5d6a7" : isAgent ? "#90caf9" : "#e0e0e0"))
                .set("margin-bottom", "8px")
                .set("align-self", isAgent || isAi ? "flex-end" : "flex-start");

        Span sender = new Span((msg.getSenderName() != null ? msg.getSenderName() : nvl(msg.getSenderType()))
                + "  •  " + formatDate(msg.getCreatedAt()));
        sender.getStyle()
                .set("font-size", "11px")
                .set("color", "#9e9e9e")
                .set("display", "block")
                .set("margin-bottom", "4px");

        Span content = new Span(msg.getContent() != null ? msg.getContent() : "");
        content.getStyle()
                .set("white-space", "pre-wrap")
                .set("font-size", "14px");

        bubble.add(sender, content);

        Div wrapper = new Div(bubble);
        wrapper.getStyle()
                .set("display", "flex")
                .set("justify-content", isAgent || isAi ? "flex-end" : "flex-start")
                .set("width", "100%");
        return wrapper;
    }

    // ── Reply ─────────────────────────────────────────────────────────────────

    private void sendReply(String text) {
        try {
            String userId = currentAuthentication.getUser().getUsername();
            ReplyRequest req = new ReplyRequest(text, userId);
            helpdeskService.replyToTicket(ticketId, req);
            replyArea.clear();
            loadMessages();
            notifications.create("Reply sent").withType(Notifications.Type.SUCCESS).show();
        } catch (Exception ex) {
            notifications.create("Failed to send reply: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR).show();
        }
    }

    // ── Styling helpers ───────────────────────────────────────────────────────

    private void applyStatusStyle(Span span, String status) {
        span.setText(status != null ? status : "");
        span.getStyle()
                .set("border-radius", "12px")
                .set("padding", "3px 12px")
                .set("font-size", "13px")
                .set("font-weight", "600")
                .set("color", "white")
                .set("background", statusColor(status));
    }

    private void applyPriorityStyle(Span span, String priority) {
        span.setText(priority != null ? priority : "");
        span.getStyle()
                .set("border-radius", "4px")
                .set("padding", "3px 10px")
                .set("font-size", "13px")
                .set("font-weight", "500")
                .set("background", priorityBg(priority))
                .set("color", priorityFg(priority));
    }

    private String statusColor(String s) {
        if (s == null) return "#9e9e9e";
        return switch (s.toLowerCase()) {
            case "open"     -> "#1976d2";
            case "pending"  -> "#f57c00";
            case "resolved" -> "#388e3c";
            case "closed"   -> "#757575";
            default         -> "#9e9e9e";
        };
    }

    private String priorityBg(String p) {
        if (p == null) return "#f5f5f5";
        return switch (p.toLowerCase()) {
            case "urgent" -> "#fce4ec";
            case "high"   -> "#fff3e0";
            case "medium" -> "#e3f2fd";
            case "low"    -> "#f1f8e9";
            default       -> "#f5f5f5";
        };
    }

    private String priorityFg(String p) {
        if (p == null) return "#757575";
        return switch (p.toLowerCase()) {
            case "urgent" -> "#c62828";
            case "high"   -> "#e65100";
            case "medium" -> "#1565c0";
            case "low"    -> "#558b2f";
            default       -> "#757575";
        };
    }

    private String nvl(String v) { return v != null ? v : "—"; }

    private String formatDate(String raw) {
        if (raw == null) return "—";
        return raw.length() > 16 ? raw.substring(0, 16).replace("T", " ") : raw;
    }
}
