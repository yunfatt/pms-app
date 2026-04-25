package com.company.pmsmain.helpdesk.view;

import com.company.pmsmain.helpdesk.dto.ChatMessage;
import com.company.pmsmain.helpdesk.service.HelpdeskRestService;
import com.company.pmsmain.multicompany.service.TenantDebugService;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ViewController("helpdesk_HelpdeskChatView")
@ViewDescriptor("helpdesk-chat-view.xml")
@Route(value = "helpdesk-chat", layout = MainView.class)
public class HelpdeskChatView extends StandardView {

    private static final Logger log = LoggerFactory.getLogger(HelpdeskChatView.class);

    // ── Spring beans ──────────────────────────────────────────────────────────
    @Autowired private HelpdeskRestService   helpdeskService;
    @Autowired private CurrentAuthentication currentAuth;
    @Autowired private TenantDebugService    tenantDebugService;

    // ── ViewComponent injections (must match XML ids exactly) ─────────────────
    @ViewComponent private VerticalLayout messagesBox;
    @ViewComponent private TextArea       messageInput;
    @ViewComponent private Button         sendBtn;
    @ViewComponent private Button         refreshBtn;
    @ViewComponent private Span           ticketLabel;
    @ViewComponent private Span           statusBadge;

    // ── Session state ─────────────────────────────────────────────────────────
    private String  ticketId;
    private String  userId;
    private String  hdUserId;
    private boolean isViewActive = false;

    private final AtomicInteger            knownMessageCount = new AtomicInteger(0);
    private final ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "helpdesk-poll");
            t.setDaemon(true);
            return t;
        });
    private ScheduledFuture<?> pollFuture;

    // ── Jmix lifecycle ────────────────────────────────────────────────────────

    @Subscribe
    public void onInit(InitEvent event) {
        log.info("onInit — instance={}", System.identityHashCode(this));

        sendBtn.addClickListener(e -> handleSend());

        if (refreshBtn != null) {
            refreshBtn.addClickListener(e -> refreshMessages());
        }

        // Ask for browser notification permission once, 3 s after the view loads
        getUI().ifPresent(ui -> ui.getPage().executeJs("""
            if ('Notification' in window && Notification.permission === 'default') {
                setTimeout(function() { Notification.requestPermission(); }, 3000);
            }
        """));
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        isViewActive = true;
        log.info("onBeforeShow — instance={}", System.identityHashCode(this));

        if (!currentAuth.isSet()) {
            log.warn("onBeforeShow: no auth context, cannot init session");
            setOfflineState();
            return;
        }

        String pmsUserId = currentAuth.getUser().getUsername();
        String companyCode;
        try {
            companyCode = tenantDebugService.getCurrentCompanyCode();
        } catch (Exception ex) {
            log.error("getCurrentCompanyCode() threw: {}", ex.getMessage(), ex);
            companyCode = null;
        }

        if (companyCode == null || companyCode.isBlank()) {
            companyCode = "DEFAULT";
        }

        log.info("PMS context: user={} company={}", pmsUserId, companyCode);

        String[] result = helpdeskService.pmsLogin(pmsUserId, companyCode);
        log.info("pmsLogin result: {}",
                result != null ? result[0] + " / " + result[1] : "NULL");

        if (result == null) {
            log.error("pmsLogin returned null — HelpDesk unavailable");
            setOfflineState();
            return;
        }

        this.ticketId = result[0];
        this.hdUserId = result[1];
        this.userId   = result[1];

        log.info("Session set: ticketId={} hdUserId={}", this.ticketId, this.hdUserId);

        ticketLabel.setText(companyCode + " | " + pmsUserId
                + " | Ticket: " + ticketId.substring(0, Math.min(8, ticketId.length())) + "…");
        statusBadge.setText("open");
        statusBadge.getElement().setAttribute("theme", "badge success");

        refreshMessages();
        startPolling();

        // Clear unread badge whenever the chat view is opened
        getUI().ifPresent(ui -> ui.getPage().executeJs("""
            var btn = document.getElementById('hd-float-btn');
            if (btn) {
                btn.classList.remove('hd-has-unread');
                var span = btn.querySelector('span');
                if (span) span.textContent = 'Help';
            }
        """));
    }

    @Subscribe
    public void onDetach(DetachEvent event) {
        isViewActive = false;
        stopPolling();
        scheduler.shutdown();
    }

    // ── Send ──────────────────────────────────────────────────────────────────

    private void handleSend() {
        log.info("handleSend — instance={} ticketId={} hdUserId={}",
                System.identityHashCode(this), ticketId, hdUserId);

        String text = messageInput.getValue();
        if (text == null || text.isBlank()) return;
        messageInput.clear();

        if (ticketId == null || hdUserId == null) {
            log.error("Cannot send — session not initialised (ticketId={} hdUserId={})",
                    ticketId, hdUserId);
            addBubble("system", "⚠️ Session not ready. Please close and reopen the chat.", "");
            return;
        }

        addBubble("user", text.trim(), "now");
        knownMessageCount.incrementAndGet(); // track the optimistic user bubble
        sendBtn.setEnabled(false);

        UI ui = UI.getCurrent();
        final String capturedTicketId = this.ticketId;
        final String capturedUserId   = this.hdUserId;
        final String capturedText     = text.trim();

        Thread.ofVirtual().start(() -> {
            try {
                log.info("Calling sendChatMessage: user={} ticket={}",
                        capturedUserId, capturedTicketId);
                String reply = helpdeskService.sendChatMessage(
                        capturedUserId, capturedTicketId, capturedText);
                log.info("Reply received: {}", reply);
                ui.access(() -> {
                    if (reply != null && !reply.isBlank()) {
                        addBubble("assistant", reply, "now");
                        knownMessageCount.incrementAndGet(); // track the optimistic AI bubble
                    } else {
                        addBubble("system", "⚠️ No response received.", "now");
                    }
                    sendBtn.setEnabled(true);
                    scrollToBottom();
                    // Do NOT call getMessages() here — that would silently skip
                    // any support reply that arrived between send and now.
                });
            } catch (Exception e) {
                log.error("sendChatMessage failed", e);
                ui.access(() -> {
                    addBubble("system", "⚠️ Error: " + e.getMessage(), "now");
                    sendBtn.setEnabled(true);
                });
            }
        });
    }

    // ── Messages ──────────────────────────────────────────────────────────────

    private void refreshMessages() {
        if (ticketId == null) return;
        List<ChatMessage> msgs = helpdeskService.getMessages(ticketId);
        messagesBox.removeAll();
        for (ChatMessage m : msgs) {
            addBubble(m.getSender(), m.getMessage(), m.getTimestamp());
        }
        knownMessageCount.set(msgs.size());
        scrollToBottom();
    }

    private void addBubble(String sender, String message, String timestamp) {
        boolean isUser   = "user".equalsIgnoreCase(sender);
        boolean isSystem = "system".equalsIgnoreCase(sender);

        Div bubble = new Div();
        bubble.addClassNames("helpdesk-bubble",
                isUser   ? "helpdesk-bubble--user"   :
                isSystem ? "helpdesk-bubble--system"  :
                           "helpdesk-bubble--assistant");

        Span senderSpan = new Span(formatSender(sender));
        senderSpan.addClassName("helpdesk-bubble__sender");

        Div bodyDiv = new Div();
        bodyDiv.addClassName("helpdesk-bubble__body");
        bodyDiv.setText(message != null ? message : "");

        if (timestamp != null && !timestamp.isBlank() && !"now".equals(timestamp)) {
            Span tsSpan = new Span(formatTimestamp(timestamp));
            tsSpan.addClassName("helpdesk-bubble__ts");
            bubble.add(senderSpan, bodyDiv, tsSpan);
        } else {
            bubble.add(senderSpan, bodyDiv);
        }

        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");
        row.setPadding(false);
        row.setSpacing(false);
        if (isUser) {
            row.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        }
        row.add(bubble);
        messagesBox.add(row);
    }

    private String formatSender(String sender) {
        if (sender == null) return "";
        return switch (sender.toLowerCase()) {
            case "user"      -> "You";
            case "assistant" -> "AI Assistant";
            case "support"   -> "🛠 Support";
            case "system"    -> "System";
            default          -> sender;
        };
    }

    private String formatTimestamp(String ts) {
        if (ts == null || ts.length() < 16) return ts != null ? ts : "";
        try {
            String[] parts = ts.replace("T", " ").split(" ");
            if (parts.length >= 2) return parts[1].substring(0, 5);
        } catch (Exception ignored) {}
        return ts;
    }

    private void scrollToBottom() {
        getUI().ifPresent(u -> u.getPage().executeJs(
                "var s = document.getElementById('chatScroller');" +
                "if(s) s.scrollTop = s.scrollHeight;"));
    }

    // ── Polling ───────────────────────────────────────────────────────────────

    private void startPolling() {
        stopPolling(); // cancel any previous future before creating a new one
        pollFuture = scheduler.scheduleAtFixedRate(() -> {
            if (ticketId == null) return;
            try {
                List<ChatMessage> msgs = helpdeskService.getMessages(ticketId);
                int known = knownMessageCount.get();
                if (msgs.size() > known) {
                    List<ChatMessage> newMsgs = msgs.subList(known, msgs.size());
                    getUI().ifPresent(ui -> ui.access(() -> {
                        for (ChatMessage m : newMsgs) {
                            if (!"user".equalsIgnoreCase(m.getSender())) {
                                addBubble(m.getSender(), m.getMessage(), m.getTimestamp());
                                if ("support".equalsIgnoreCase(m.getSender())) {
                                    notifyNewSupportMessage(m.getMessage());
                                }
                            }
                        }
                        // Always advance the counter to avoid re-processing messages
                        knownMessageCount.set(msgs.size());
                        scrollToBottom();
                    }));
                }
            } catch (Exception e) {
                log.warn("Poll failed for ticket {}: {}", ticketId, e.getMessage());
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    private void stopPolling() {
        if (pollFuture != null && !pollFuture.isCancelled()) {
            pollFuture.cancel(false);
            pollFuture = null;
        }
    }

    private void notifyNewSupportMessage(String message) {
        getUI().ifPresent(ui -> ui.getPage().executeJs("""
            (function(msg) {
                // 1. Red badge on floating Help button
                var btn = document.getElementById('hd-float-btn');
                if (btn) btn.classList.add('hd-has-unread');

                // 2. Update button label
                var span = btn ? btn.querySelector('span') : null;
                if (span) span.textContent = 'Help (1 new)';

                // 3. Browser push notification when tab is not focused
                if (document.hidden || !document.hasFocus()) {
                    if ('Notification' in window) {
                        if (Notification.permission === 'granted') {
                            new Notification('HelpDesk Support Reply', {
                                body: msg.substring(0, 100),
                                icon: '/icons/icon.png',
                                tag:  'helpdesk-support'
                            });
                        } else if (Notification.permission !== 'denied') {
                            Notification.requestPermission().then(function(p) {
                                if (p === 'granted') {
                                    new Notification('HelpDesk Support Reply', {
                                        body: msg.substring(0, 100),
                                        icon: '/icons/icon.png',
                                        tag:  'helpdesk-support'
                                    });
                                }
                            });
                        }
                    }

                    // 4. Flash browser tab title 5 times
                    var original = document.title;
                    var count = 0;
                    var flash = setInterval(function() {
                        document.title = count % 2 === 0
                            ? '\\uD83D\\uDEE0 New Support Reply!'
                            : original;
                        count++;
                        if (count > 10) {
                            clearInterval(flash);
                            document.title = original;
                        }
                    }, 800);
                }
            })($0);
        """, message));
    }

    private void setOfflineState() {
        ticketLabel.setText("⚠️ HelpDesk service unavailable");
        statusBadge.setText("offline");
        statusBadge.getElement().setAttribute("theme", "badge error");
        sendBtn.setEnabled(false);
        messageInput.setEnabled(false);
        addBubble("system",
                "Unable to connect to HelpDesk. Please try again later.", "");
    }
}
