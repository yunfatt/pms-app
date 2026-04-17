package com.company.pmsmain.view.reports;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.entity.Property;
import com.company.pmsmain.multicompany.context.TenantContext;
import com.company.pmsmain.view.main.MainView;
import com.company.pmsmain.view.property.PropertyListView;
import com.stimulsoft.report.StiExportManager;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.dictionary.databases.StiPostgreSQLDatabase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.download.DownloadFormat;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Route(value = "ageing-report", layout = MainView.class)
@ViewController(id = "AgeingReport.view")
@ViewDescriptor(path = "ageing-report-view.xml")
public class AgeingReportView extends StandardView {

    @Autowired
    private Notifications notifications;

    @Autowired
    private Downloader downloader;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    @Autowired
    private DialogWindows dialogWindows;

    @Value("${app.reports.path:com/company/pmsmain/Reports}")
    private String reportsBasePath;

    @Value("${app.system-name:Property Management System}")
    private String systemName;

    @Value("${app.company-name:}")
    private String companyName;

    // ── Report parameters ────────────────────────────────────────────────────
    private String propertyNo1 = "";
    private String propertyNo2 = "ZZZZZZ";
    private String runDate     = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private String useDueDate  = "0";

    private enum ExportType { PDF, EXCEL }

    @Subscribe
    public void onInit(InitEvent event) {
        showParameterDialog();
    }

    // ── Step 1: Parameter dialog ─────────────────────────────────────────────

    private void showParameterDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("480px");
        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(true);

        H4 title = new H4("Ageing Report — Parameters");
        title.getStyle().set("margin", "0 0 16px 0");

        // ── Property No From ─────────────────────────────────────────────────
        TextField propertyNo1Field = new TextField("Property No From");
        propertyNo1Field.setWidthFull();
        propertyNo1Field.setPlaceholder("Leave blank for all");
        propertyNo1Field.setValue(propertyNo1);

        Button lookupFrom = new Button(VaadinIcon.SEARCH.create());
        lookupFrom.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        lookupFrom.setTooltipText("Lookup Property");
        lookupFrom.addClickListener(e -> openPropertyLookup(propertyNo1Field));

        HorizontalLayout fromRow = new HorizontalLayout(propertyNo1Field, lookupFrom);
        fromRow.setAlignItems(FlexComponent.Alignment.END);
        fromRow.setWidthFull();
        fromRow.expand(propertyNo1Field);

        // ── Property No To ───────────────────────────────────────────────────
        TextField propertyNo2Field = new TextField("Property No To");
        propertyNo2Field.setWidthFull();
        propertyNo2Field.setPlaceholder("Leave blank for all");
        propertyNo2Field.setValue(propertyNo2.equals("ZZZZZZ") ? "" : propertyNo2);

        Button lookupTo = new Button(VaadinIcon.SEARCH.create());
        lookupTo.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        lookupTo.setTooltipText("Lookup Property");
        lookupTo.addClickListener(e -> openPropertyLookup(propertyNo2Field));

        HorizontalLayout toRow = new HorizontalLayout(propertyNo2Field, lookupTo);
        toRow.setAlignItems(FlexComponent.Alignment.END);
        toRow.setWidthFull();
        toRow.expand(propertyNo2Field);

        // ── Date format: display dd/MM/yyyy, send yyyy-MM-dd to report ───────────
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("dd/MM/yyyy");

        // ── Run Date ─────────────────────────────────────────────────────────
        DatePicker runDatePicker = new DatePicker("Run Date");
        runDatePicker.setI18n(i18n);
        runDatePicker.setWidthFull();
        runDatePicker.setValue(LocalDate.now());
        runDatePicker.setRequired(true);

        // ── Use Due Date ─────────────────────────────────────────────────────
        Checkbox useDueDateCheck = new Checkbox("Use Due Date");
        useDueDateCheck.setValue(false);

        // ── Form layout ──────────────────────────────────────────────────────
        FormLayout form = new FormLayout(
                fromRow,
                toRow,
                runDatePicker,
                useDueDateCheck
        );
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        form.setWidthFull();

        // ── Buttons ──────────────────────────────────────────────────────────
        Button nextBtn = new Button("Next", VaadinIcon.ARROW_RIGHT.create());
        nextBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextBtn.addClickListener(e -> {
            if (runDatePicker.getValue() == null) {
                notifications.create("Run Date is required.")
                        .withType(Notifications.Type.WARNING)
                        .show();
                return;
            }

            // Save parameters
            propertyNo1 = propertyNo1Field.getValue().trim();
            propertyNo2 = propertyNo2Field.getValue().isBlank()
                    ? "ZZZZZZ"
                    : propertyNo2Field.getValue().trim();
            runDate    = runDatePicker.getValue()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            useDueDate = useDueDateCheck.getValue() ? "1" : "0";

            dialog.close();
            showFormatDialog();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.addClickListener(e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(cancelBtn, nextBtn);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.setWidthFull();
        buttons.getStyle().set("margin-top", "16px");

        VerticalLayout content = new VerticalLayout(title, form, buttons);
        content.setPadding(true);
        content.setSpacing(false);

        dialog.add(content);
        dialog.open();
    }

    // ── Property lookup ───────────────────────────────────────────────────────

    private void openPropertyLookup(TextField targetField) {
        DialogWindow<PropertyListView> lookupDialog = dialogWindows
                .lookup(this, Property.class)
                .withViewClass(PropertyListView.class)
                .withSelectHandler(properties -> {
                    if (!properties.isEmpty()) {
                        Property selected = properties.iterator().next();
                        targetField.setValue(
                                selected.getPropertyno() != null
                                        ? selected.getPropertyno()
                                        : "");
                    }
                })
                .build();
        lookupDialog.open();
    }

    // ── Step 2: Format dialog ────────────────────────────────────────────────

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

        Button backBtn = new Button("Back", VaadinIcon.ARROW_LEFT.create());
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backBtn.addClickListener(e -> {
            dialog.close();
            showParameterDialog();
        });

        HorizontalLayout formatButtons = new HorizontalLayout(pdfBtn, excelBtn);
        formatButtons.setSpacing(true);
        formatButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        formatButtons.setWidthFull();

        HorizontalLayout bottomRow = new HorizontalLayout(backBtn);
        bottomRow.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        bottomRow.setWidthFull();

        VerticalLayout content = new VerticalLayout(title, formatButtons, bottomRow);
        content.setPadding(true);
        content.setSpacing(false);
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        dialog.add(content);
        dialog.open();
    }

    // ── Export logic ─────────────────────────────────────────────────────────

    private void exportReport(ExportType exportType) {
        try {
            StiReport report = buildReport();
            if (report == null) return;

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            switch (exportType) {
                case PDF -> {
                    StiExportManager.exportPdf(report, out);
                    downloader.download(out.toByteArray(), "AgeingReport.pdf", DownloadFormat.PDF);
                }
                case EXCEL -> {
                    StiExportManager.exportExcel(report, out);
                    downloader.download(out.toByteArray(), "AgeingReport.xlsx", DownloadFormat.XLSX);
                }
            }
            // Navigate back to main page after download
            getUI().ifPresent(ui -> ui.navigate(""));

        } catch (Throwable ex) {
            notifications.create("Failed to generate report: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR)
                    .show();
            throw new RuntimeException("Failed to generate Ageing report", ex);
        }
    }

    // ── Template loader ───────────────────────────────────────────────────────

    private File loadReportTemplate(String fileName) throws IOException {
        if (reportsBasePath.startsWith("/")) {
            Path path = Paths.get(reportsBasePath, fileName);
            if (!Files.exists(path)) {
                throw new FileNotFoundException(
                        "Report template not found at: " + path);
            }
            return path.toFile();
        }

        String resourcePath = reportsBasePath + "/" + fileName;
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new FileNotFoundException(
                    "Report template not found on classpath: " + resourcePath);
        }
        return resource.getFile();
    }

    // ── Report builder ───────────────────────────────────────────────────────

    private StiReport buildReport() throws Exception {
        // 1. Resolve company
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

        // 2. Build connection string
        String dbConn = "Server="      + company.getDbHost()
                + ";Port="     + company.getDbPort()
                + ";Database=" + company.getDbName()
                + ";User Id=" + company.getDbUsername()
                + ";Password=" + company.getDbPasswordEnc() + ";";
        System.out.println(">>> Report DB Connection: " + dbConn);
        // 3. Load report template
        File reportFile = loadReportTemplate("pm_ageing.mrt");
        StiReport report = StiSerializeManager.deserializeReport(reportFile);

        // 4. Set database connection
        report.getDictionary().getDatabases().clear();
        report.getDictionary().getDatabases().add(
                new StiPostgreSQLDatabase("pms", "pms", dbConn));
        report.getDictionary().synchronize();

        // 5. Set variables
        report.setVariable("SystemName",  systemName);
        report.setVariable("CompanyName", company.getCompanyName());
        report.setVariable("UserId",      currentAuthentication.getUser().getUsername());
        report.setVariable("PropertyNo1", propertyNo1);
        report.setVariable("PropertyNo2", propertyNo2);
        report.setVariable("RunDate",     runDate);
        report.setVariable("UseDueDate",  useDueDate);

        // 6. Render
        report.render();

        return report;
    }
}