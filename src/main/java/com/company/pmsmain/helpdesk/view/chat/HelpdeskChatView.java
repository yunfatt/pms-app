package com.company.pmsmain.helpdesk.view.chat;

import com.company.pmsmain.helpdesk.dto.ChatRequest;
import com.company.pmsmain.helpdesk.dto.ChatResponse;
import com.company.pmsmain.helpdesk.dto.CreateTicketRequest;
import com.company.pmsmain.helpdesk.dto.ProgressDto;
import com.company.pmsmain.helpdesk.service.HelpdeskService;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "helpdesk-chat-v1", layout = MainView.class)
@ViewController(id = "Helpdesk.chat")
@ViewDescriptor(path = "helpdesk-chat-view.xml")
public class HelpdeskChatView extends StandardView {

    @ViewComponent private VerticalLayout  chatMessagesBox;
    @ViewComponent private HorizontalLayout typingIndicator;
    @ViewComponent private TextArea        messageInput;

    @Autowired private HelpdeskService       helpdeskService;
    @Autowired private Notifications         notifications;
    @Autowired private CurrentAuthentication currentAuthentication;

    private final List<ChatMessage> history = new ArrayList<>();
    private volatile Thread progressPollerThread;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Subscribe
    public void onInit(InitEvent event) {
        messageInput.addKeyPressListener(Key.ENTER, e -> onSend(null));
        appendSystemMessage("Hello! I'm the Advelsoft AI Support Assistant. How can I help you today?");
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Subscribe
    public void onDetach(DetachEvent event) {
        Thread t = progressPollerThread;
        if (t != null) t.interrupt();
    }

    // ── Button handlers ───────────────────────────────────────────────────────

    @Subscribe("sendButton")
    public void onSend(ClickEvent<JmixButton> e) {
        String text = messageInput.getValue();
        if (text == null || text.isBlank()) return;
        messageInput.clear();
        sendMessage(text.trim());
    }

    @Subscribe("clearChatButton")
    public void onClearChat(ClickEvent<JmixButton> e) {
        history.clear();
        chatMessagesBox.removeAll();
        appendSystemMessage("Chat cleared. How can I help you?");
    }

    @Subscribe("newTicketFromChatBtn")
    public void onNewTicketFromChat(ClickEvent<JmixButton> e) {
        if (history.isEmpty()) {
            notifications.create("Start a conversation first before creating a ticket.")
                    .withType(Notifications.Type.WARNING).show();
            return;
        }
        createTicketFromChat();
    }

    // ── Message sending ───────────────────────────────────────────────────────

    private void sendMessage(String text) {
        history.add(new ChatMessage("user", text));
        appendBubble("You", text, "user");
        setTyping(true);

        try {
            String userId = currentAuthentication.getUser().getUsername();
            ChatRequest req = new ChatRequest(text, userId);
            ChatResponse resp = helpdeskService.sendChat(req);

            if (resp == null) {
                setTyping(false);
                appendSystemMessage("No response from AI assistant. Please try again.");
                return;
            }
            if (resp.getQueueId() != null && !resp.getQueueId().isBlank()) {
                pollProgress(resp.getQueueId());
            } else {
                String reply = resp.getReply() != null ? resp.getReply() : "(no response)";
                handleAiReply(reply);
            }
        } catch (Exception ex) {
            setTyping(false);
            notifications.create("Chat API error: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR).show();
            appendSystemMessage("Error: Could not reach the AI assistant. Please try again.");
        }
    }

    private void pollProgress(String queueId) {
        Thread t = new Thread(() -> {
            int attempts = 0;
            while (attempts < 30) {
                try {
                    Thread.sleep(2000);
                    ProgressDto progress = helpdeskService.getProgress(queueId);
                    if (progress == null) { attempts++; continue; }

                    if (progress.isComplete()) {
                        String result = progress.getResult() != null ? progress.getResult() : "(no result)";
                        getUI().ifPresent(ui -> ui.access(() -> handleAiReply(result)));
                        return;
                    }
                    if (progress.isError()) {
                        String err = progress.getError() != null ? progress.getError() : "Unknown error";
                        getUI().ifPresent(ui -> ui.access(() -> {
                            setTyping(false);
                            appendSystemMessage("AI error: " + err);
                        }));
                        return;
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception ex) {
                    attempts++;
                }
            }
            getUI().ifPresent(ui -> ui.access(() -> {
                setTyping(false);
                appendSystemMessage("AI response timed out. Please try again.");
            }));
        }, "helpdesk-progress-poller");
        t.setDaemon(true);
        progressPollerThread = t;
        t.start();
    }

    private void handleAiReply(String reply) {
        setTyping(false);
        history.add(new ChatMessage("ai", reply));
        appendBubble("AI Assistant", reply, "ai");
        scrollToBottom();
    }

    // ── Create ticket from chat ───────────────────────────────────────────────

    private void createTicketFromChat() {
        try {
            StringBuilder sb = new StringBuilder();
            for (ChatMessage m : history) {
                sb.append("[").append(m.role.toUpperCase()).append("] ").append(m.text).append("\n\n");
            }
            String subject = history.get(0).text;
            if (subject.length() > 80) subject = subject.substring(0, 80) + "...";

            String userId = currentAuthentication.getUser().getUsername();
            CreateTicketRequest req = new CreateTicketRequest(
                    subject, sb.toString(), "medium", null, null, userId);
            var ticket = helpdeskService.createTicket(req);
            notifications.create("Ticket #" + ticket.getId() + " created from this chat")
                    .withType(Notifications.Type.SUCCESS).show();
        } catch (Exception ex) {
            notifications.create("Failed to create ticket: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR).show();
        }
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    private void appendBubble(String senderLabel, String text, String role) {
        boolean isRight = "user".equalsIgnoreCase(role);

        Div bubble = new Div();
        bubble.getStyle()
                .set("max-width", "70%")
                .set("padding", "10px 14px")
                .set("border-radius", isRight ? "12px 12px 4px 12px" : "12px 12px 12px 4px")
                .set("background", isRight ? "#e3f2fd" : "user".equalsIgnoreCase(role) ? "#e3f2fd" : "#e8f5e9")
                .set("border", "1px solid " + (isRight ? "#90caf9" : "#a5d6a7"))
                .set("word-break", "break-word");

        Span label = new Span(senderLabel);
        label.getStyle()
                .set("font-size", "11px")
                .set("color", "#9e9e9e")
                .set("font-weight", "600")
                .set("display", "block")
                .set("margin-bottom", "4px");

        Span content = new Span(text);
        content.getStyle()
                .set("white-space", "pre-wrap")
                .set("font-size", "14px");

        bubble.add(label, content);

        Div row = new Div(bubble);
        row.getStyle()
                .set("display", "flex")
                .set("justify-content", isRight ? "flex-end" : "flex-start")
                .set("width", "100%")
                .set("margin-bottom", "8px");

        chatMessagesBox.add(row);
    }

    private void appendSystemMessage(String text) {
        Div msg = new Div();
        Span span = new Span(text);
        span.getStyle()
                .set("font-size", "13px")
                .set("color", "#757575")
                .set("font-style", "italic");
        msg.add(span);
        msg.getStyle()
                .set("text-align", "center")
                .set("padding", "6px 0")
                .set("width", "100%");
        chatMessagesBox.add(msg);
    }

    private void setTyping(boolean typing) {
        typingIndicator.setVisible(typing);
    }

    private void scrollToBottom() {
        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "setTimeout(() => { var el = document.querySelector('.helpdesk-chat-messages'); " +
                "if(el) el.scrollTop = el.scrollHeight; }, 100);"));
    }

    // ── Internal model ────────────────────────────────────────────────────────

    private record ChatMessage(String role, String text) {}
}
