package com.company.pmsmain.helpdesk.view.ticket;

import com.company.pmsmain.helpdesk.dto.CreateTicketRequest;
import com.company.pmsmain.helpdesk.dto.TicketDto;
import com.company.pmsmain.helpdesk.service.HelpdeskService;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "helpdesk-tickets-v1", layout = MainView.class)
@ViewController(id = "Helpdesk.tickets")
@ViewDescriptor(path = "ticket-list-view.xml")
public class TicketListView extends StandardView {

    @ViewComponent private VerticalLayout gridContainer;
    @ViewComponent private TypedTextField<String> searchField;

    @Autowired private HelpdeskService helpdeskService;
    @Autowired private Notifications notifications;
    @Autowired private CurrentAuthentication currentAuthentication;

    private Grid<TicketDto> grid;
    private List<TicketDto> allTickets = new ArrayList<>();
    private String statusFilter = "";

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Subscribe
    public void onInit(InitEvent event) {
        buildGrid();
        searchField.addKeyPressListener(Key.ENTER, e -> applyFilter());
        searchField.addValueChangeListener(e -> {
            if (e.getValue() == null || e.getValue().isBlank()) applyFilter();
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        loadTickets();
    }

    // ── Button handlers ───────────────────────────────────────────────────────

    @Subscribe("searchButton")
    public void onSearch(ClickEvent<JmixButton> e) { applyFilter(); }

    @Subscribe("newTicketButton")
    public void onNewTicket(ClickEvent<JmixButton> e) { showNewTicketDialog(); }

    @Subscribe("refreshButton")
    public void onRefresh(ClickEvent<JmixButton> e) { loadTickets(); }

    @Subscribe("statusAllBtn")
    public void onStatusAll(ClickEvent<JmixButton> e)      { statusFilter = "";         applyFilter(); }

    @Subscribe("statusOpenBtn")
    public void onStatusOpen(ClickEvent<JmixButton> e)     { statusFilter = "open";     applyFilter(); }

    @Subscribe("statusPendingBtn")
    public void onStatusPending(ClickEvent<JmixButton> e)  { statusFilter = "pending";  applyFilter(); }

    @Subscribe("statusResolvedBtn")
    public void onStatusResolved(ClickEvent<JmixButton> e) { statusFilter = "resolved"; applyFilter(); }

    @Subscribe("statusClosedBtn")
    public void onStatusClosed(ClickEvent<JmixButton> e)   { statusFilter = "closed";   applyFilter(); }

    // ── Grid construction ─────────────────────────────────────────────────────

    private void buildGrid() {
        grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeight("500px");

        grid.addColumn(t -> "#" + t.getId())
                .setHeader("ID").setWidth("80px").setFlexGrow(0).setSortable(true);

        grid.addColumn(TicketDto::getSubject)
                .setHeader("Subject").setFlexGrow(2).setSortable(true);

        grid.addComponentColumn(t -> statusBadge(t.getStatus()))
                .setHeader("Status").setWidth("110px").setFlexGrow(0);

        grid.addComponentColumn(t -> priorityBadge(t.getPriority()))
                .setHeader("Priority").setWidth("100px").setFlexGrow(0);

        grid.addColumn(t -> t.getCustomerName() != null ? t.getCustomerName() : "")
                .setHeader("Customer").setWidth("180px").setSortable(true);

        grid.addColumn(t -> formatDate(t.getCreatedAt()))
                .setHeader("Created").setWidth("160px");

        grid.addItemClickListener(e -> {
            if (e.getItem() != null && e.getItem().getId() != null) {
                RouteParameters params = new RouteParameters(
                        "ticketId", String.valueOf(e.getItem().getId()));
                getUI().ifPresent(ui -> ui.navigate(TicketDetailView.class, params));
            }
        });

        gridContainer.add(grid);
        gridContainer.expand(grid);
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    private void loadTickets() {
        try {
            allTickets = helpdeskService.getTickets();
        } catch (Exception ex) {
            notifications.create("Cannot connect to HelpDesk API: " + ex.getMessage())
                    .withType(Notifications.Type.WARNING)
                    .show();
            allTickets = new ArrayList<>();
        }
        applyFilter();
    }

    private void applyFilter() {
        String keyword = searchField.getValue() == null ? "" : searchField.getValue().trim().toLowerCase();
        List<TicketDto> filtered = allTickets.stream()
                .filter(t -> statusFilter.isEmpty()
                        || statusFilter.equalsIgnoreCase(t.getStatus()))
                .filter(t -> keyword.isEmpty()
                        || contains(t.getSubject(), keyword)
                        || contains(t.getCustomerName(), keyword))
                .collect(Collectors.toList());
        grid.setItems(filtered);
    }

    private boolean contains(String field, String keyword) {
        return field != null && field.toLowerCase().contains(keyword);
    }

    // ── New Ticket dialog ─────────────────────────────────────────────────────

    private void showNewTicketDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("480px");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(true);

        H4 title = new H4("New Support Ticket");
        title.getStyle().set("margin", "0 0 16px 0");

        TextField subjectField = new TextField("Subject");
        subjectField.setWidthFull();
        subjectField.setRequired(true);

        TextArea descField = new TextArea("Description");
        descField.setWidthFull();
        descField.setMinRows(4);

        Select<String> prioritySelect = new Select<>();
        prioritySelect.setLabel("Priority");
        prioritySelect.setItems("low", "medium", "high", "urgent");
        prioritySelect.setValue("medium");
        prioritySelect.setWidthFull();

        TextField customerField = new TextField("Customer Name");
        customerField.setWidthFull();

        Button submitBtn = new Button("Submit Ticket");
        submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitBtn.addClickListener(e -> {
            if (subjectField.isEmpty()) {
                notifications.create("Subject is required")
                        .withType(Notifications.Type.WARNING).show();
                return;
            }
            submitNewTicket(subjectField.getValue(), descField.getValue(),
                    prioritySelect.getValue(), customerField.getValue(), dialog);
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.addClickListener(e -> dialog.close());

        HorizontalLayout actionRow = new HorizontalLayout(submitBtn, cancelBtn);
        actionRow.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        actionRow.setWidthFull();

        VerticalLayout content = new VerticalLayout(title, subjectField, descField,
                prioritySelect, customerField, actionRow);
        content.setPadding(true);
        content.setSpacing(true);
        dialog.add(content);
        dialog.open();
    }

    private void submitNewTicket(String subject, String description, String priority,
                                  String customerName, Dialog dialog) {
        try {
            String userId = currentAuthentication.getUser().getUsername();
            CreateTicketRequest req = new CreateTicketRequest(
                    subject, description, priority, null, customerName, userId);
            TicketDto created = helpdeskService.createTicket(req);
            dialog.close();
            notifications.create("Ticket #" + created.getId() + " created successfully")
                    .withType(Notifications.Type.SUCCESS).show();
            loadTickets();
        } catch (Exception ex) {
            notifications.create("Failed to create ticket: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR).show();
        }
    }

    // ── Badge helpers ─────────────────────────────────────────────────────────

    private Span statusBadge(String status) {
        Span s = new Span(status != null ? status : "");
        s.getStyle()
                .set("border-radius", "12px")
                .set("padding", "2px 10px")
                .set("font-size", "12px")
                .set("font-weight", "600")
                .set("color", "white")
                .set("background", statusColor(status));
        return s;
    }

    private String statusColor(String status) {
        if (status == null) return "#9e9e9e";
        return switch (status.toLowerCase()) {
            case "open"     -> "#1976d2";
            case "pending"  -> "#f57c00";
            case "resolved" -> "#388e3c";
            case "closed"   -> "#757575";
            default         -> "#9e9e9e";
        };
    }

    private Span priorityBadge(String priority) {
        Span s = new Span(priority != null ? priority : "");
        s.getStyle()
                .set("border-radius", "4px")
                .set("padding", "2px 8px")
                .set("font-size", "12px")
                .set("font-weight", "500")
                .set("background", priorityBg(priority))
                .set("color", priorityFg(priority));
        return s;
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

    private String formatDate(String raw) {
        if (raw == null) return "";
        return raw.length() > 16 ? raw.substring(0, 16).replace("T", " ") : raw;
    }
}
