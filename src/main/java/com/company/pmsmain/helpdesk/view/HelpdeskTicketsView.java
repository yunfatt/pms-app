package com.company.pmsmain.helpdesk.view;

import com.company.pmsmain.helpdesk.dto.HelpdeskTicket;
import com.company.pmsmain.helpdesk.service.HelpdeskRestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.company.pmsmain.view.main.MainView;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ViewController("helpdesk_HelpdeskTicketsView")
@ViewDescriptor("helpdesk-tickets-view.xml")
@Route(value = "helpdesk-tickets", layout = MainView.class)
public class HelpdeskTicketsView extends StandardView {

    @ViewComponent private Button  refreshTicketsBtn;
    @ViewComponent private Span    ticketCountLabel;
    @ViewComponent private Div     ticketsGridContainer;

    @Autowired private HelpdeskRestService helpdeskService;

    private Grid<HelpdeskTicket> ticketsGrid;

    @Subscribe
    public void onInit(InitEvent event) {
        buildGrid();
        refreshTicketsBtn.addClickListener(e -> loadTickets());
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        loadTickets();
    }

    private void buildGrid() {
        ticketsGrid = new Grid<>();
        ticketsGrid.setSizeFull();

        ticketsGrid.addColumn(t -> truncate(t.getTicketId(), 12))
            .setHeader("Ticket ID")
            .setAutoWidth(true);

        ticketsGrid.addColumn(HelpdeskTicket::getUserId)
            .setHeader("User")
            .setAutoWidth(true);

        ticketsGrid.addComponentColumn(t -> {
            Span badge = new Span(t.getStatus());
            badge.getElement().setAttribute("theme",
                "closed".equalsIgnoreCase(t.getStatus())
                    ? "badge contrast" : "badge success");
            return badge;
        }).setHeader("Status").setAutoWidth(true);

        ticketsGrid.addColumn(t -> shortTs(t.getCreatedAt()))
            .setHeader("Created").setAutoWidth(true);

        ticketsGrid.addColumn(t -> shortTs(t.getLastUpdated()))
            .setHeader("Updated").setAutoWidth(true);

        ticketsGrid.addComponentColumn(t -> {
            Span badge = new Span(t.isSupportAlerted() ? "Yes" : "No");
            badge.getElement().setAttribute("theme",
                t.isSupportAlerted() ? "badge error" : "badge");
            return badge;
        }).setHeader("Escalated").setAutoWidth(true).setFlexGrow(0);

        ticketsGrid.addComponentColumn(this::buildActionButtons)
            .setHeader("").setAutoWidth(true).setFlexGrow(0);

        ticketsGridContainer.add(ticketsGrid);
        ticketsGridContainer.setHeight("100%");
        ticketsGridContainer.getStyle().set("display", "flex");
        ticketsGridContainer.getStyle().set("flex-direction", "column");
    }

    private void loadTickets() {
        List<HelpdeskTicket> tickets = helpdeskService.listTickets();
        ticketsGrid.setItems(tickets);
        ticketCountLabel.setText(
            tickets.size() + " ticket" + (tickets.size() == 1 ? "" : "s")
        );
    }

    private HorizontalLayout buildActionButtons(HelpdeskTicket ticket) {
        Button viewBtn = new Button(VaadinIcon.EYE.create());
        viewBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY,
                                  ButtonVariant.LUMO_SMALL);
        viewBtn.setTooltipText("View transcript");
        viewBtn.addClickListener(e -> openTranscriptDialog(ticket));

        Button replyBtn = new Button(VaadinIcon.REPLY.create());
        replyBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                                   ButtonVariant.LUMO_SMALL);
        replyBtn.setTooltipText("Reply");
        replyBtn.addClickListener(e -> openReplyDialog(ticket));
        if ("closed".equalsIgnoreCase(ticket.getStatus())) {
            replyBtn.setEnabled(false);
        }

        HorizontalLayout hl = new HorizontalLayout(viewBtn, replyBtn);
        hl.setSpacing(true);
        hl.setPadding(false);
        return hl;
    }

    private void openTranscriptDialog(HelpdeskTicket ticket) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Ticket: " + truncate(ticket.getTicketId(), 12));
        dialog.setWidth("600px");
        dialog.setHeight("500px");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        var messages = helpdeskService.getMessages(ticket.getTicketId());
        if (messages.isEmpty()) {
            content.add(new Span("No messages yet."));
        } else {
            for (var m : messages) {
                Span line = new Span(
                    m.getSender().toUpperCase() + ": " + m.getMessage()
                );
                line.getStyle()
                    .set("display", "block")
                    .set("padding", "4px 0")
                    .set("border-bottom", "1px solid #eee");
                content.add(line);
            }
        }

        Button closeBtn = new Button("Close", e -> dialog.close());
        dialog.add(content);
        dialog.getFooter().add(closeBtn);
        dialog.open();
    }

    private void openReplyDialog(HelpdeskTicket ticket) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Reply to " + ticket.getUserId());
        dialog.setWidth("480px");

        TextArea replyArea = new TextArea("Your message");
        replyArea.setPlaceholder("Type your reply here...");
        replyArea.setWidth("100%");
        replyArea.setMinRows(4);

        Button sendBtn = new Button("Send", VaadinIcon.PAPERPLANE.create());
        sendBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendBtn.addClickListener(e -> {
            String text = replyArea.getValue();
            if (text == null || text.isBlank()) return;
            boolean ok = helpdeskService.sendReply(
                ticket.getTicketId(), text);
            dialog.close();
            Notification n = Notification.show(
                ok ? "Reply sent." : "Failed to send reply.");
            n.addThemeVariants(ok
                ? NotificationVariant.LUMO_SUCCESS
                : NotificationVariant.LUMO_ERROR);
            loadTickets();
        });

        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        dialog.add(new VerticalLayout(replyArea));
        dialog.getFooter().add(cancelBtn, sendBtn);
        dialog.open();
    }

    private String truncate(String s, int len) {
        if (s == null) return "";
        return s.length() <= len ? s : s.substring(0, len) + "…";
    }

    private String shortTs(String ts) {
        if (ts == null || ts.length() < 16) return ts != null ? ts : "";
        try {
            String[] parts = ts.split("[ T]");
            String datePart = parts[0];
            String timePart = parts.length > 1
                ? parts[1].substring(0, 5) : "";
            String[] dp = datePart.split("-");
            String[] months = {"","Jan","Feb","Mar","Apr","May","Jun",
                               "Jul","Aug","Sep","Oct","Nov","Dec"};
            int m = Integer.parseInt(dp[1]);
            return dp[2] + " " + months[m] + " " + timePart;
        } catch (Exception ignored) {}
        return ts;
    }
}
