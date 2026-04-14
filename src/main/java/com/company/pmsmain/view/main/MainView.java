package com.company.pmsmain.view.main;

import com.company.pmsmain.entity.User;
import com.company.pmsmain.multicompany.service.CompanyRoutingTestService;
import com.company.pmsmain.multicompany.service.TenantDebugService;
import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import io.jmix.core.Messages;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

@Route("")
@ViewController(id = "MainView")
@ViewDescriptor(path = "main-view.xml")
public class MainView extends StandardMainView implements AfterNavigationObserver {

    @Autowired
    private CompanyRoutingTestService companyRoutingTestService;
    @Autowired
    private Messages messages;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private CurrentUserSubstitution currentUserSubstitution;
    @Autowired
    private TenantDebugService tenantDebugService;

    @ViewComponent
    private Span currentCompanyLabel;
    @ViewComponent
    private HorizontalLayout topTabsBox;
    @ViewComponent
    private VerticalLayout dynamicMenuBox;

    private Tabs moduleTabs;
    private String currentRoute = "";

    @Subscribe
    public void onInit(InitEvent event) {
        buildTopTabs();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        updateCompanyBadge();
        if (moduleTabs.getSelectedTab() == null) {
            moduleTabs.setSelectedIndex(0);
        }
        rebuildCurrentMenu();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        currentRoute = event.getLocation().getPath();
        rebuildCurrentMenu();
    }

    private void updateCompanyBadge() {
        String code = tenantDebugService.getCurrentCompanyCode();
        String name = tenantDebugService.getCurrentCompanyName();
        currentCompanyLabel.setText(String.format("%s - %s",
                Strings.nullToEmpty(code),
                Strings.nullToEmpty(name)));
    }

    private void buildTopTabs() {
        Tab arTab = new Tab("Accounts Receivable");
        Tab apTab = new Tab("Accounts Payable");
        Tab cbTab = new Tab("Cash Book");
        Tab glTab = new Tab("General Ledger");
        Tab adminTab = new Tab("Administration");

        moduleTabs = new Tabs(arTab, apTab, cbTab, glTab, adminTab);
        moduleTabs.setWidthFull();
        moduleTabs.addClassName("premium-module-tabs");
        moduleTabs.addSelectedChangeListener(e -> rebuildCurrentMenu());

        topTabsBox.removeAll();
        topTabsBox.add(moduleTabs);
    }

    private void rebuildCurrentMenu() {
        if (moduleTabs == null || moduleTabs.getSelectedTab() == null) return;

        String label = moduleTabs.getSelectedTab().getLabel();
        dynamicMenuBox.removeAll();

        switch (label) {
            case "Accounts Receivable" -> showAccountsReceivableMenu();
            case "Accounts Payable" -> showAccountsPayableMenu();
            case "Cash Book" -> showCashBookMenu();
            case "General Ledger" -> showGeneralLedgerMenu();
            case "Administration" -> showAdministrationMenu();
        }
    }

    private void showAccountsReceivableMenu() {
        dynamicMenuBox.add(
                createSection("AR_M", "MASTER",
                        createMenuButton("Phase", "phases", VaadinIcon.BUILDING),
                        createMenuButton("Property", "properties", VaadinIcon.HOME),
                        createMenuButton("Customers", "customers", VaadinIcon.USER),
                        createMenuButton("Transaction Code", "ardocs", VaadinIcon.USER)
                ),
                createSection("AR_T", "TRANSACTION",
                        createMenuButton("Invoice Entry", "invhdrs", VaadinIcon.FILE_TEXT),
                        createMenuButton("Receipt Entry", "receipts", VaadinIcon.MONEY)
                )
        );
    }

    private void showAccountsPayableMenu() {
        dynamicMenuBox.add(
                createSection("AP_M", "MASTER", createMenuButton("Suppliers", "suppliers", VaadinIcon.TRUCK)),
                createSection("AP_T", "TRANSACTION", createMenuButton("Bill Entry", "bills", VaadinIcon.INPUT))
        );
    }

    private void showCashBookMenu() {
        dynamicMenuBox.add(
                createSection("CB_T", "TRANSACTION",
                        createMenuButton("Bank Entry", "bank-entry", VaadinIcon.INSTITUTION),
                        createMenuButton("Reconciliation", "reconcile", VaadinIcon.CHECK_CIRCLE)
                )
        );
    }

    private void showGeneralLedgerMenu() {
        dynamicMenuBox.add(
                createSection("GL_M", "MASTER", createMenuButton("Chart of Accounts", "accounts", VaadinIcon.LIST)),
                createSection("GL_R", "REPORTS", createMenuButton("Trial Balance", "trial-balance", VaadinIcon.BAR_CHART))
        );
    }

    private void showAdministrationMenu() {
        dynamicMenuBox.add(
                createSection("ADM_SEC", "SECURITY",
                        createMenuButton("Users", "users", VaadinIcon.USERS),
                        // Added Missing Role Views
                        createMenuButton("Resource Roles", "sec/resourcerolemodels", VaadinIcon.KEY),
                        createMenuButton("Row-level Roles", "sec/rowlevelrolemodels", VaadinIcon.LOCK)
                )
        );
    }

    private Component createSection(String key, String title, Component... items) {
        VerticalLayout section = uiComponents.create(VerticalLayout.class);
        section.setPadding(false);
        section.setSpacing(false);
        section.addClassName("premium-menu-section");

        HorizontalLayout header = uiComponents.create(HorizontalLayout.class);
        header.addClassName("premium-menu-section-header");
        header.setSpacing(false);
        header.setWidthFull();

        Span arrow = uiComponents.create(Span.class);
        arrow.addClassName("premium-menu-section-arrow");

        Span caption = uiComponents.create(Span.class);
        caption.setText(title);
        caption.addClassName("premium-menu-section-caption");

        header.add(arrow, caption);

        VerticalLayout itemsBox = uiComponents.create(VerticalLayout.class);
        itemsBox.setPadding(false);
        itemsBox.setSpacing(false);
        itemsBox.addClassName("premium-menu-items-box");
        for (Component item : items) itemsBox.add(item);

        boolean expanded = getSectionState(key, true);
        itemsBox.setVisible(expanded);
        arrow.setText(expanded ? "▾ " : "▸ ");

        header.addClickListener(e -> {
            boolean newState = !itemsBox.isVisible();
            setSectionState(key, newState);
            itemsBox.setVisible(newState);
            arrow.setText(newState ? "▾ " : "▸ ");
        });

        section.add(header, itemsBox);
        return section;
    }

    private Button createMenuButton(String caption, String route, VaadinIcon icon) {
        Button btn = uiComponents.create(Button.class);
        btn.setText(caption);
        btn.setIcon(icon.create());
        btn.setWidthFull();

        btn.addClassName("premium-menu-item");
        // Explicit check for high-contrast border application
        if (route.equals(currentRoute)) {
            btn.addClassName("premium-menu-item-active");
        }

        btn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(route)));
        return btn;
    }

    private boolean getSectionState(String key, boolean def) {
        return getUI().map(ui -> {
            Object val = ui.getSession().getAttribute(key);
            return val instanceof Boolean ? (Boolean) val : def;
        }).orElse(def);
    }

    private void setSectionState(String key, boolean val) {
        getUI().ifPresent(ui -> ui.getSession().setAttribute(key, val));
    }

    @Install(to = "userMenu", subject = "buttonRenderer")
    private Component userMenuButtonRenderer(final UserDetails userDetails) {
        if (!(userDetails instanceof User user)) return null;
        HorizontalLayout layout = uiComponents.create(HorizontalLayout.class);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        Avatar avatar = uiComponents.create(Avatar.class);
        avatar.setName(user.getUsername());
        avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
        Span name = uiComponents.create(Span.class);
        name.setText(user.getUsername());
        layout.add(avatar, name);
        return layout;
    }
}