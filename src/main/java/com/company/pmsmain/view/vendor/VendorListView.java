package com.company.pmsmain.view.vendor;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.entity.Vendor;
import com.company.pmsmain.multicompany.context.TenantContext;
import com.company.pmsmain.view.main.MainView;
import com.stimulsoft.report.StiExportManager;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.dictionary.databases.StiPostgreSQLDatabase;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.genericfilter.GenericFilter;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.download.DownloadFormat;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

@Route(value = "suppliers", layout = MainView.class)
@ViewController(id = "Vendor.list")
@ViewDescriptor(path = "vendor-list-view.xml")
@LookupComponent("vendorsDataGrid")
@DialogMode(width = "64em")
public class VendorListView extends StandardListView<Vendor> {

    @ViewComponent
    private DataGrid<Vendor> vendorsDataGrid;

    @ViewComponent
    private JmixButton printButton;

    @ViewComponent
    private TypedTextField<String> searchField;

    @ViewComponent
    private JmixButton searchButton;

    @ViewComponent
    private JmixButton advancedSearchToggle;

    @ViewComponent
    private GenericFilter genericFilter;

    @ViewComponent
    private CollectionLoader<Vendor> vendorsDl;

    @Autowired
    private Notifications notifications;

    @Autowired
    private Downloader downloader;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    @Value("${app.reports.path:com/company/pmsmain/Reports}")
    private String reportsBasePath;

    @Value("${app.system-name:Property Management System}")
    private String systemName;

    @Value("${app.company-name:}")
    private String companyName;

    private boolean advancedMode = false;

    private enum ExportType { PDF, EXCEL }

    @Subscribe
    public void onInit(InitEvent event) {
        printButton.addClickListener(e -> showFormatDialog());
        searchField.addKeyPressListener(Key.ENTER, e -> executeSearch());
        searchField.addValueChangeListener(e -> {
            if (e.getValue() == null || e.getValue().isBlank()) {
                executeSearch();
            }
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        vendorsDl.setParameter("searchText", "");
        vendorsDl.load();
    }

    @Subscribe("searchButton")
    public void onSearchButtonClick(ClickEvent<JmixButton> event) {
        executeSearch();
    }

    @Subscribe("advancedSearchToggle")
    public void onAdvancedSearchToggle(ClickEvent<JmixButton> event) {
        advancedMode = !advancedMode;

        if (advancedMode) {
            advancedSearchToggle.setText("Simple");
            advancedSearchToggle.setIcon(VaadinIcon.SEARCH.create());
        } else {
            advancedSearchToggle.setText("Advanced");
            advancedSearchToggle.setIcon(VaadinIcon.FILTER.create());
        }

        searchField.setVisible(!advancedMode);
        searchButton.setVisible(!advancedMode);
        genericFilter.setVisible(advancedMode);

        if (!advancedMode) {
            vendorsDl.setParameter("searchText", "");
            vendorsDl.load();
        } else {
            vendorsDl.removeParameter("searchText");
        }
    }

    private void executeSearch() {
        String text = searchField.getValue();
        vendorsDl.setParameter("searchText", text == null ? "" : text.trim());
        vendorsDl.load();
    }

    // ── Format selection dialog ───────────────────────────────────────────────

    private void showFormatDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("320px");
        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);

        H4 title = new H4("Select Export Format");
        title.getStyle().set("margin", "0 0 16px 0");

        Button pdfBtn = new Button("PDF", VaadinIcon.FILE_TEXT.create());
        pdfBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        pdfBtn.setWidth("120px");
        pdfBtn.addClickListener(e -> {
            dialog.close();
            exportReport(ExportType.PDF);
        });

        Button excelBtn = new Button("Excel", VaadinIcon.TABLE.create());
        excelBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        excelBtn.setWidth("120px");
        excelBtn.addClickListener(e -> {
            dialog.close();
            exportReport(ExportType.EXCEL);
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.addClickListener(e -> dialog.close());

        HorizontalLayout formatButtons = new HorizontalLayout(pdfBtn, excelBtn);
        formatButtons.setSpacing(true);
        formatButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        formatButtons.setWidthFull();

        HorizontalLayout cancelRow = new HorizontalLayout(cancelBtn);
        cancelRow.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        cancelRow.setWidthFull();

        VerticalLayout content = new VerticalLayout(title, formatButtons, cancelRow);
        content.setPadding(true);
        content.setSpacing(false);
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(content);
        dialog.open();
    }

    // ── Export logic ──────────────────────────────────────────────────────────

    private void exportReport(ExportType exportType) {
        try {
            StiReport report = buildReport();
            if (report == null) return;

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            switch (exportType) {
                case PDF -> {
                    StiExportManager.exportPdf(report, out);
                    downloader.download(out.toByteArray(), "SupplierList.pdf", DownloadFormat.PDF);
                }
                case EXCEL -> {
                    StiExportManager.exportExcel(report, out);
                    downloader.download(out.toByteArray(), "SupplierList.xlsx", DownloadFormat.XLSX);
                }
            }
            getUI().ifPresent(ui -> ui.navigate(""));

        } catch (Throwable ex) {
            notifications.create("Failed to generate report: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR)
                    .show();
            throw new RuntimeException("Failed to generate Supplier List report", ex);
        }
    }

    // ── Template loader ───────────────────────────────────────────────────────

    private File loadReportTemplate(String fileName) throws IOException {
        if (reportsBasePath.startsWith("/")) {
            Path path = Paths.get(reportsBasePath, fileName);
            if (!Files.exists(path)) {
                throw new FileNotFoundException("Report template not found at: " + path);
            }
            return path.toFile();
        }
        String resourcePath = reportsBasePath + "/" + fileName;
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new FileNotFoundException("Report template not found on classpath: " + resourcePath);
        }
        return resource.getFile();
    }

    // ── Report builder ────────────────────────────────────────────────────────

    private StiReport buildReport() throws Exception {
        String rawCode = TenantContext.getCompanyCode();
        if (rawCode == null || rawCode.isBlank()) {
            notifications.create("No tenant/company selected.")
                    .withType(Notifications.Type.ERROR)
                    .show();
            return null;
        }
        String companyCode = rawCode.trim().toUpperCase();

        AppCompany company = unconstrainedDataManager.load(AppCompany.class)
                .query("select c from AppCompany c where c.companyCode = :code")
                .parameter("code", companyCode)
                .optional()
                .orElse(null);

        if (company == null) {
            notifications.create("Company not found: " + companyCode)
                    .withType(Notifications.Type.ERROR)
                    .show();
            return null;
        }

        String dbConn = "Server="   + company.getDbHost()
                + ";Port="     + company.getDbPort()
                + ";Database=" + company.getDbName()
                + ";User Id="  + company.getDbUsername()
                + ";Password=" + company.getDbPasswordEnc() + ";";

        File reportFile = loadReportTemplate("supplierlist.mrt");
        StiReport report = StiSerializeManager.deserializeReport(reportFile);

        report.getDictionary().getDatabases().clear();
        report.getDictionary().getDatabases().add(
                new StiPostgreSQLDatabase("pms", dbConn));

        Collection<Vendor> vendors = vendorsDataGrid.getItems().getItems();
        String vdrNoFrom = "";
        String vdrNoTo   = "ZZZZZZZZ";
        if (!vendors.isEmpty()) {
            List<Vendor> sorted = vendors.stream()
                    .filter(v -> v.getId() != null && v.getId().getVdrno() != null)
                    .sorted((a, b) -> a.getId().getVdrno().compareTo(b.getId().getVdrno()))
                    .toList();
            if (!sorted.isEmpty()) {
                vdrNoFrom = sorted.get(0).getId().getVdrno();
                vdrNoTo   = sorted.get(sorted.size() - 1).getId().getVdrno();
            }
        }

        report.setVariable("SystemName",  systemName);
        report.setVariable("CompanyName", company.getCompanyName());
        report.setVariable("UserId",      currentAuthentication.getUser().getUsername());
        report.setVariable("VdrNoFrom",   vdrNoFrom);
        report.setVariable("VdrNoTo",     vdrNoTo);

        report.render();
        return report;
    }
}
