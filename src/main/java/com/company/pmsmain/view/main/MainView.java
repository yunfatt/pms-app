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
    private Messages messages;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private CurrentUserSubstitution currentUserSubstitution;
    @Autowired
    private TenantDebugService tenantDebugService;
    @Autowired
    private CompanyRoutingTestService companyRoutingTestService;

    @ViewComponent
    private Span currentCompanyLabel;
    @ViewComponent
    private HorizontalLayout topTabsBox;
    @ViewComponent
    private VerticalLayout dynamicMenuBox;

    private Tabs moduleTabs;
    private Tabs subTabs;
    private Div menuItemsBox = new Div();
    private String currentRoute = "";

    @Subscribe
    public void onInit(InitEvent event) {
        buildModuleTabs();
        getContent().getStyle()
                .set("background-color", "#f0f9f4");
      }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        updateCompanyBadge();
        if (moduleTabs.getSelectedTab() == null) {
            moduleTabs.setSelectedIndex(0);
        }
        rebuildSubTabs();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        currentRoute = event.getLocation().getPath();
        rebuildMenuItems();
    }

    // ── Company badge ─────────────────────────────────────────────────────────

    private void updateCompanyBadge() {
        String code = tenantDebugService.getCurrentCompanyCode();
        String name = tenantDebugService.getCurrentCompanyName();
        currentCompanyLabel.setText(String.format("%s - %s",
                Strings.nullToEmpty(code),
                Strings.nullToEmpty(name)));
    }

    // ── Level 1: Module tabs ──────────────────────────────────────────────────

    private void buildModuleTabs() {
        Tab arTab  = new Tab("Accounts Receivable");
        Tab apTab  = new Tab("Accounts Payable");
        Tab cbTab  = new Tab("Cash Book");
        Tab glTab  = new Tab("General Ledger");
        Tab admTab = new Tab("Administration");

        moduleTabs = new Tabs(arTab, apTab, cbTab, glTab, admTab);
        moduleTabs.setWidthFull();
        moduleTabs.addClassName("premium-module-tabs");
        moduleTabs.addSelectedChangeListener(e -> rebuildSubTabs());

        topTabsBox.removeAll();
        topTabsBox.add(moduleTabs);
    }

    // ── Level 2: Sub-tabs ─────────────────────────────────────────────────────

    private void rebuildSubTabs() {
        if (moduleTabs == null || moduleTabs.getSelectedTab() == null) return;

        Tab filesTab   = new Tab("Files");
        Tab trxnTab    = new Tab("Transactions");
        Tab reportsTab = new Tab("Reports");

        subTabs = new Tabs(filesTab, trxnTab, reportsTab);
        subTabs.setWidthFull();
        subTabs.addClassName("premium-sub-tabs");
        subTabs.addSelectedChangeListener(e -> {
            menuItemsBox.removeAll();
            rebuildMenuItems();
        });

        dynamicMenuBox.removeAll();
        dynamicMenuBox.add(subTabs);
        dynamicMenuBox.add(createMenuItemsBox());

        subTabs.setSelectedIndex(0);
    }

    // ── Level 3: Menu items ───────────────────────────────────────────────────

    private Component createMenuItemsBox() {
        menuItemsBox = new Div();
        menuItemsBox.setWidthFull();
        menuItemsBox.getStyle()
                .set("padding", "8px 0")
                .set("display", "flex")
                .set("flex-direction", "column");
        rebuildMenuItems();
        return menuItemsBox;
    }

    private void rebuildMenuItems() {
        if (moduleTabs == null || subTabs == null) return;
        if (moduleTabs.getSelectedTab() == null
                || subTabs.getSelectedTab() == null) return;

        String module = moduleTabs.getSelectedTab().getLabel();
        String subTab = getSubTabLabel();

        menuItemsBox.removeAll();

        switch (module) {
            case "Accounts Receivable" -> buildARMenu(subTab);
            case "Accounts Payable"    -> buildAPMenu(subTab);
            case "Cash Book"           -> buildCBMenu(subTab);
            case "General Ledger"      -> buildGLMenu(subTab);
            case "Administration"      -> buildAdminMenu(subTab);
        }
    }

    private String getSubTabLabel() {
        if (subTabs == null || subTabs.getSelectedTab() == null) return "Files";
        return subTabs.getSelectedTab().getLabel();
    }

    // ── AR Menu ───────────────────────────────────────────────────────────────

    private void buildARMenu(String subTab) {
        switch (subTab) {
            case "Files" -> {
                menuItemsBox.add(
                        createMenuButton("Phase",            "phases"),
                        createMenuButton("Property",         "properties"),
                        createMenuButton("Customers",        "customers"),
                        createMenuButton("Transaction Code", "ardocs"),
                        createMenuButton("Charge Code",      "charges")
                );
            }
            case "Transactions" -> {
                menuItemsBox.add(
                        createMenuButton("Invoice Entry", "invhdrs")
                );
            }
            case "Reports" -> {
                menuItemsBox.add(
                        createMenuButton("Customer Transactions", "cust-trxn-report"),
                        createMenuButton("Ageing",                "ageing-report")
                );
            }
        }
    }

    // ── AP Menu ───────────────────────────────────────────────────────────────

    private void buildAPMenu(String subTab) {
        switch (subTab) {
            case "Files"        -> menuItemsBox.add(createDummyMenuButton("Suppliers"));
            case "Transactions" -> menuItemsBox.add(createDummyMenuButton("Bill Entry"));
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("AP Reports"));
        }
    }

    // ── CB Menu ───────────────────────────────────────────────────────────────

    private void buildCBMenu(String subTab) {
        switch (subTab) {
            case "Files"        -> menuItemsBox.add(createDummyMenuButton("Bank Accounts"));
            case "Transactions" -> {
                menuItemsBox.add(
                        createDummyMenuButton("Bank Entry"),
                        createDummyMenuButton("Reconciliation")
                );
            }
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("Cash Book Report"));
        }
    }

    // ── GL Menu ───────────────────────────────────────────────────────────────

    private void buildGLMenu(String subTab) {
        switch (subTab) {
            case "Files"        -> menuItemsBox.add(createDummyMenuButton("Chart of Accounts"));
            case "Transactions" -> menuItemsBox.add(createDummyMenuButton("Journal Entry"));
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("Trial Balance"));
        }
    }

    // ── Admin Menu ────────────────────────────────────────────────────────────

    private void buildAdminMenu(String subTab) {
        switch (subTab) {
            case "Files" -> {
                menuItemsBox.add(
                        createMenuButton("Users",           "users"),
                        createMenuButton("Resource Roles",  "sec/resourcerolemodels"),
                        createMenuButton("Row-level Roles", "sec/rowlevelrolemodels")
                );
            }
            case "Transactions" -> menuItemsBox.add(createDummyMenuButton("Admin Tasks"));
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("Audit Log"));
        }
    }

    // ── Button helpers ────────────────────────────────────────────────────────

    private Button createMenuButton(String caption, String route) {
        Button btn = uiComponents.create(Button.class);
        btn.setText(caption);
        btn.setWidthFull();
        btn.addClassName("premium-menu-item");
        if (route.equals(currentRoute)) {
            btn.addClassName("premium-menu-item-active");
        }
        btn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(route)));
        return btn;
    }

    private Button createDummyMenuButton(String caption) {
        String slug = caption.toLowerCase().replace(" ", "-");
        return createMenuButton(caption, "not-implemented/" + slug);
    }

    // ── User menu renderer ────────────────────────────────────────────────────

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

    private boolean getSectionState(String key, boolean def) {
        return getUI().map(ui -> {
            Object val = ui.getSession().getAttribute(key);
            return val instanceof Boolean ? (Boolean) val : def;
        }).orElse(def);
    }

    private void setSectionState(String key, boolean val) {
        getUI().ifPresent(ui -> ui.getSession().setAttribute(key, val));
    }
}