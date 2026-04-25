package com.company.pmsmain.view.main;

import com.company.pmsmain.entity.User;
import com.company.pmsmain.multicompany.service.CompanyRoutingTestService;
import com.company.pmsmain.multicompany.service.TenantDebugService;
import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
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

    // ── Theme constants ───────────────────────────────────────────────────────
    private static final String THEME_BLUE        = "pms-blue";
    private static final String THEME_DARK        = "pms-dark";
    private static final String THEME_GREEN       = "pms-green";
    private static final String THEME_SESSION_KEY = "pmsAppTheme";

    @Autowired private Messages messages;
    @Autowired private UiComponents uiComponents;
    @Autowired private CurrentUserSubstitution currentUserSubstitution;
    @Autowired private TenantDebugService tenantDebugService;
    @Autowired private CompanyRoutingTestService companyRoutingTestService;

    @ViewComponent private Span currentCompanyLabel;
    @ViewComponent private HorizontalLayout topTabsBox;
    @ViewComponent private VerticalLayout dynamicMenuBox;

    private Tabs   moduleTabs;
    private Tabs   subTabs;
    private Div    menuItemsBox         = new Div();
    private String currentRoute         = "";
    private Button themeToggleBtn;
    private String currentTheme         = THEME_BLUE;
    private boolean themeToggleAttached  = false;
    private boolean floatingBtnAttached  = false;

    @Subscribe
    public void onInit(InitEvent event) {
        buildModuleTabs();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        updateCompanyBadge();

        // Attach theme toggle only once — component tree is fully ready here
        if (!themeToggleAttached) {
            buildThemeToggle();
            themeToggleAttached = true;
        }

        // Apply saved theme — defaults to pms-blue on first visit
        applyTheme(getSavedTheme());

        if (!floatingBtnAttached) {
            buildFloatingChatButton();
            floatingBtnAttached = true;
        }

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

    // ── Theme management ──────────────────────────────────────────────────────

    private void buildThemeToggle() {
        themeToggleBtn = new Button(VaadinIcon.MOON.create());
        themeToggleBtn.addClassName("theme-toggle-btn");
        themeToggleBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        themeToggleBtn.setTooltipText("Switch to Dark theme");
        themeToggleBtn.addClickListener(e -> cycleTheme());

        // topTabsBox parent is the topBar HorizontalLayout declared in XML.
        // Component tree is fully attached by onBeforeShow, so getParent()
        // is guaranteed to return the correct element here.
        topTabsBox.getParent().ifPresent(parent -> {
            if (parent instanceof HorizontalLayout topBar) {
                // Insert just before the last component (userMenu)
                int insertAt = Math.max(0, topBar.getComponentCount() - 1);
                topBar.addComponentAtIndex(insertAt, themeToggleBtn);
            }
        });
    }

    private void cycleTheme() {
        String next = switch (currentTheme) {
            case THEME_BLUE  -> THEME_DARK;
            case THEME_DARK  -> THEME_GREEN;
            default          -> THEME_BLUE;
        };
        applyTheme(next);
        saveTheme(next);
    }

    private void applyTheme(String theme) {
        currentTheme = theme;
        UI.getCurrent().getElement().setAttribute("data-theme", theme);

        if (themeToggleBtn == null) return;

        // Icon and tooltip hint at the NEXT theme in the cycle
        switch (theme) {
            case THEME_BLUE -> {
                themeToggleBtn.setIcon(VaadinIcon.MOON.create());
                themeToggleBtn.setTooltipText("Switch to Dark theme");
            }
            case THEME_DARK -> {
                themeToggleBtn.setIcon(VaadinIcon.GLOBE.create());
                themeToggleBtn.setTooltipText("Switch to Green theme");
            }
            default -> {
                themeToggleBtn.setIcon(VaadinIcon.PAINT_ROLL.create());
                themeToggleBtn.setTooltipText("Switch to Blue theme");
            }
        }
    }

    private String getSavedTheme() {
        return getUI().map(ui -> {
            Object val = ui.getSession().getAttribute(THEME_SESSION_KEY);
            return val instanceof String s ? s : THEME_BLUE;
        }).orElse(THEME_BLUE);
    }

    private void saveTheme(String theme) {
        getUI().ifPresent(ui ->
                ui.getSession().setAttribute(THEME_SESSION_KEY, theme));
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
        Tab hdTab  = new Tab("Help Desk");

        moduleTabs = new Tabs(arTab, apTab, cbTab, glTab, admTab, hdTab);
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
            case "Help Desk"           -> buildHelpdeskMenu(subTab);
        }
    }

    private String getSubTabLabel() {
        if (subTabs == null || subTabs.getSelectedTab() == null) return "Files";
        return subTabs.getSelectedTab().getLabel();
    }

    // ── AR Menu ───────────────────────────────────────────────────────────────

    private void buildARMenu(String subTab) {
        switch (subTab) {
            case "Files" -> menuItemsBox.add(
                    createMenuButton("Phase",            "phases"),
                    createMenuButton("Property",         "properties"),
                    createMenuButton("Customers",        "customers"),
                    createMenuButton("Transaction Code", "ardocs"),
                    createMenuButton("Charge Code",      "charges")
            );
            case "Transactions" -> menuItemsBox.add(
                    createMenuButton("Invoice Entry", "invhdrs")
            );
            case "Reports" -> menuItemsBox.add(
                    createMenuButton("Customer Transactions", "cust-trxn-report"),
                    createMenuButton("Ageing",                "ageing-report")
            );
        }
    }

    // ── AP Menu ───────────────────────────────────────────────────────────────

    private void buildAPMenu(String subTab) {
        switch (subTab) {
            case "Files"        -> menuItemsBox.add(createMenuButton("Suppliers",  "suppliers"));
            case "Transactions" -> menuItemsBox.add(createDummyMenuButton("Bill Entry"));
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("AP Reports"));
        }
    }

    // ── CB Menu ───────────────────────────────────────────────────────────────

    private void buildCBMenu(String subTab) {
        switch (subTab) {
            case "Files"        -> menuItemsBox.add(createMenuButton("Banks", "banks"));
            case "Transactions" -> menuItemsBox.add(
                    createDummyMenuButton("Bank Entry"),
                    createDummyMenuButton("Reconciliation")
            );
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("Cash Book Report"));
        }
    }

    // ── GL Menu ───────────────────────────────────────────────────────────────

    private void buildGLMenu(String subTab) {
        switch (subTab) {
            case "Files"        -> menuItemsBox.add(
                    createMenuButton("GL Accounts", "glaccounts"),
                    createMenuButton("Fiscal Year",  "glfyears")
            );
            case "Transactions" -> menuItemsBox.add(createDummyMenuButton("Journal Entry"));
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("Trial Balance"));
        }
    }

    // ── Admin Menu ────────────────────────────────────────────────────────────

    private void buildAdminMenu(String subTab) {
        switch (subTab) {
            case "Files" -> menuItemsBox.add(
                    createMenuButton("Users",           "users"),
                    createMenuButton("Resource Roles",  "sec/resourcerolemodels"),
                    createMenuButton("Row-level Roles", "sec/rowlevelrolemodels"),
                    createMenuSeparator(),
                    createMenuButton("💬 HelpDesk Chat",   "helpdesk-chat"),
                    createMenuButton("🛠 Support Tickets", "helpdesk-tickets")
            );
            case "Transactions" -> menuItemsBox.add(createDummyMenuButton("Admin Tasks"));
            case "Reports"      -> menuItemsBox.add(createDummyMenuButton("Audit Log"));
        }
    }

    private Div createMenuSeparator() {
        Div sep = new Div();
        sep.addClassName("premium-menu-separator");
        sep.getStyle()
                .set("border-top", "1px solid rgba(255,255,255,0.15)")
                .set("margin", "6px 8px");
        return sep;
    }

    private void buildFloatingChatButton() {
        getUI().ifPresent(u -> u.getPage().executeJs(
            "if (document.getElementById('hd-float-btn')) return;" +
            "var st = document.createElement('style');" +
            "st.textContent = " +
            "  '@keyframes hd-pulse {" +
            "    0%,100%{ box-shadow:0 0 0 0 rgba(26,86,160,.5); }" +
            "    60%{     box-shadow:0 0 0 10px rgba(26,86,160,0); } }' +" +
            "  '#hd-float-btn {" +
            "    position:fixed; bottom:28px; right:28px; z-index:9999;" +
            "    background:#1a56a0; color:#fff; border:none;" +
            "    border-radius:28px; padding:12px 20px;" +
            "    font-size:14px; font-weight:600; cursor:pointer;" +
            "    display:flex; align-items:center; gap:8px;" +
            "    animation:hd-pulse 2.5s infinite;" +
            "    box-shadow:0 4px 16px rgba(26,86,160,.4); }' +" +
            "  '#hd-float-btn:hover { background:#1565c0; }' +" +
            "  '#hd-float-btn.hd-has-unread::after {" +
            "    content:\"\"; position:absolute; top:6px; right:6px;" +
            "    width:10px; height:10px; background:#e53935;" +
            "    border-radius:50%; border:2px solid #fff; }';" +
            "document.head.appendChild(st);" +
            "var btn = document.createElement('button');" +
            "btn.id = 'hd-float-btn';" +
            "btn.innerHTML = '<svg width=\"18\" height=\"18\" viewBox=\"0 0 24 24\" fill=\"none\" " +
            "  stroke=\"currentColor\" stroke-width=\"2\">" +
            "  <path d=\"M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z\"/>" +
            "</svg><span>Help</span>';" +
            "btn.addEventListener('click', function() {" +
            "  window.Vaadin.Flow.clients[" +
            "    Object.keys(window.Vaadin.Flow.clients)[0]" +
            "  ].navigate('helpdesk-chat');" +
            "});" +
            "document.body.appendChild(btn);"
        ));
    }

    // ── Help Desk Menu ────────────────────────────────────────────────────────

    private void buildHelpdeskMenu(String subTab) {
        switch (subTab) {
            case "Files" -> menuItemsBox.add(
                    createMenuButton("Tickets",  "helpdesk-tickets"),
                    createMenuButton("AI Chat",  "helpdesk-chat")
            );
            case "Transactions" -> menuItemsBox.add(
                    createDummyMenuButton("Helpdesk Reports")
            );
            case "Reports" -> {}
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
