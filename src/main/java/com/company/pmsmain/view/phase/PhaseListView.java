package com.company.pmsmain.view.phase;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.entity.Phase;
import com.company.pmsmain.multicompany.context.TenantContext;
import com.company.pmsmain.view.main.MainView;
import com.stimulsoft.report.StiExportManager;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.dictionary.databases.StiPostgreSQLDatabase;
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
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.download.DownloadFormat;
import io.jmix.flowui.download.Downloader;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collection;
import java.util.List;

@Route(value = "phases", layout = MainView.class)
@ViewController(id = "Phase.list")
@ViewDescriptor(path = "phase-list-view.xml")
@LookupComponent("phasesDataGrid")
@DialogMode(width = "64em")
public class PhaseListView extends StandardListView<Phase> {

    @ViewComponent
    private DataGrid<Phase> phasesDataGrid;

    @ViewComponent
    private JmixButton printButton;

    @Autowired
    private Notifications notifications;

    @Autowired
    private Downloader downloader;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    @Value("${app.reports.path:com/company/pmsmain/Reports/PrintPhaseList.mrt}")
    private String reportPath;

    @Value("${app.system-name:Property Management System}")
    private String systemName;

    @Value("${app.company-name:}")
    private String companyName;

    @Subscribe
    public void onInit(InitEvent event) {
        printButton.addClickListener(e -> showFormatDialog());
    }

    private enum ExportType { PDF, EXCEL }

    // ── Format selection popup ───────────────────────────────────────────────

    private void showFormatDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("320px");
        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);

        // Title
        H4 title = new H4("Select Export Format");
        title.getStyle().set("margin", "0 0 16px 0");

        // PDF button
        Button pdfBtn = new Button("PDF", VaadinIcon.FILE_TEXT.create());
        pdfBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        pdfBtn.setWidth("120px");
        pdfBtn.addClickListener(e -> {
            dialog.close();
            exportReport(ExportType.PDF);
        });

        // Excel button
        Button excelBtn = new Button("Excel", VaadinIcon.TABLE.create());
        excelBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        excelBtn.setWidth("120px");
        excelBtn.addClickListener(e -> {
            dialog.close();
            exportReport(ExportType.EXCEL);
        });

        // Cancel button
        Button cancelBtn = new Button("Cancel");
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.addClickListener(e -> dialog.close());

        // Format buttons row
        HorizontalLayout formatButtons = new HorizontalLayout(pdfBtn, excelBtn);
        formatButtons.setSpacing(true);
        formatButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        formatButtons.setWidthFull();

        // Cancel row
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

    // ── Export logic ─────────────────────────────────────────────────────────

    private void exportReport(ExportType exportType) {
        try {
            StiReport report = buildReport();
            if (report == null) return;

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            switch (exportType) {
                case PDF -> {
                    StiExportManager.exportPdf(report, out);
                    downloader.download(out.toByteArray(), "PrintPhaseList.pdf", DownloadFormat.PDF);
                }
                case EXCEL -> {
                    StiExportManager.exportExcel(report, out);
                    downloader.download(out.toByteArray(), "PrintPhaseList.xlsx", DownloadFormat.XLSX);
                }
            }

        } catch (Throwable ex) {
            notifications.create("Failed to generate report: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR)
                    .show();
            throw new RuntimeException("Failed to generate Phase List report", ex);
        }
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
        String dbConn = "Server="   + company.getDbHost()
                + ";Port="     + company.getDbPort()
                + ";Database=" + company.getDbName()
                + ";User Id="  + company.getDbUsername()
                + ";Password=" + company.getDbPasswordEnc() + ";";

        // 3. Load report template
        ClassPathResource classPathResource = new ClassPathResource(reportPath);
        if (!classPathResource.exists()) {
            notifications.create("Report template not found: " + reportPath)
                    .withType(Notifications.Type.ERROR)
                    .show();
            return null;
        }
        File reportFile = classPathResource.getFile();
        StiReport report = StiSerializeManager.deserializeReport(reportFile);

        // 4. Set database connection
        report.getDictionary().getDatabases().clear();
        report.getDictionary().getDatabases().add(
                new StiPostgreSQLDatabase("pms", dbConn));

        // 5. Derive range from grid
        Collection<Phase> phases = phasesDataGrid.getItems().getItems();
        String phaseNoFrom = "";
        String phaseNoTo   = "ZZZZZZ";
        if (!phases.isEmpty()) {
            List<Phase> sorted = phases.stream()
                    .filter(p -> p.getPhaseno() != null)
                    .sorted((a, b) -> a.getPhaseno().compareTo(b.getPhaseno()))
                    .toList();
            if (!sorted.isEmpty()) {
                phaseNoFrom = sorted.get(0).getPhaseno();
                phaseNoTo   = sorted.get(sorted.size() - 1).getPhaseno();
            }
        }

        // 6. Set variables
        report.setVariable("SystemName",  systemName);
        report.setVariable("CompanyName", companyName);
        report.setVariable("UserId",      currentAuthentication.getUser().getUsername());
        report.setVariable("PhaseNoFrom", phaseNoFrom);
        report.setVariable("PhaseNoTo",   phaseNoTo);

        // 7. Render
        report.render();

        return report;
    }
}
