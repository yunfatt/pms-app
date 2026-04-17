package com.company.pmsmain.view.phase;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.multicompany.context.TenantContext;
import com.stimulsoft.report.StiExportManager;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.dictionary.databases.StiPostgreSQLDatabase;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.core.security.CurrentAuthentication;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/public/report/phase-list")
public class ReportViewerController {

    @Autowired
    private UnconstrainedDataManager unconstrainedDataManager;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Value("${app.reports.path:com/company/pmsmain/Reports}")
    private String reportsBasePath;

    @Value("${app.system-name:Property Management System}")
    private String systemName;

    @Value("${app.company-name:}")
    private String companyName;

    // ── Endpoints ────────────────────────────────────────────────────────────

    @GetMapping("/pdf")
    public void renderPdf(
            @RequestParam(defaultValue = "") String phaseNoFrom,
            @RequestParam(defaultValue = "ZZZZZZ") String phaseNoTo,
            @RequestParam(defaultValue = "") String companyCode,
            HttpServletResponse response) throws Exception {

        StiReport report = buildReport(phaseNoFrom, phaseNoTo, companyCode);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StiExportManager.exportPdf(report, out);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=PrintPhaseList.pdf");
        response.getOutputStream().write(out.toByteArray());
    }

    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    public void renderHtml(
            @RequestParam(defaultValue = "") String phaseNoFrom,
            @RequestParam(defaultValue = "ZZZZZZ") String phaseNoTo,
            @RequestParam(defaultValue = "") String companyCode,
            HttpServletResponse response) throws Exception {

        StiReport report = buildReport(phaseNoFrom, phaseNoTo, companyCode);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StiExportManager.exportHtml(report, out);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(out.toString("UTF-8"));
    }

    @GetMapping(value = "/viewer", produces = MediaType.TEXT_HTML_VALUE)
    public void viewerPage(
            @RequestParam(defaultValue = "") String phaseNoFrom,
            @RequestParam(defaultValue = "ZZZZZZ") String phaseNoTo,
            @RequestParam(defaultValue = "") String companyCode,
            HttpServletResponse response) throws Exception {

        String apiUrl = "/public/report/phase-list/pdf"
                + "?phaseNoFrom=" + phaseNoFrom
                + "&phaseNoTo="   + phaseNoTo
                + "&companyCode=" + companyCode;

        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8"/>
                    <title>Phase Listing Report</title>
                    <style>
                        * { margin:0; padding:0; box-sizing:border-box; }
                        body { font-family:Arial,sans-serif; background:#404040;
                               display:flex; flex-direction:column; height:100vh; }
                        #toolbar { background:#323232; color:#fff; padding:8px 16px;
                                   display:flex; align-items:center; gap:12px;
                                   box-shadow:0 2px 4px rgba(0,0,0,0.4); flex-shrink:0; }
                        #toolbar h1 { font-size:14px; font-weight:bold; flex:1; }
                        .btn { background:#555; color:#fff; border:none; padding:6px 14px;
                               border-radius:4px; cursor:pointer; font-size:13px; }
                        .btn:hover { background:#666; }
                        .btn.primary { background:#1976d2; }
                        .btn.success { background:#388e3c; }
                        #status { color:#fff; text-align:center; padding:40px; font-size:16px; }
                        #pdfFrame { flex:1; width:100%; border:none; display:none; }
                    </style>
                </head>
                <body>
                <div id="toolbar">
                    <h1>&#128196; Phase Listing Report</h1>
                    <button class="btn primary" onclick="document.getElementById('pdfFrame').contentWindow.print()">&#128424; Print</button>
                    <a id="dlBtn" class="btn success" href="#" download="PrintPhaseList.pdf">&#8595; Download PDF</a>
                    <button class="btn" onclick="window.close()">&#x2715; Close</button>
                </div>
                <div id="status">Loading report, please wait...</div>
                <iframe id="pdfFrame"></iframe>
                <script>
                    fetch('%s', {credentials:'include'})
                        .then(r => { if(!r.ok) throw new Error('Status '+r.status); return r.blob(); })
                        .then(blob => {
                            const url = URL.createObjectURL(blob);
                            const f = document.getElementById('pdfFrame');
                            f.src = url; f.style.display='block';
                            document.getElementById('status').style.display='none';
                            document.getElementById('dlBtn').href = url;
                        })
                        .catch(e => { document.getElementById('status').textContent='Failed: '+e.message; });
                </script>
                </body></html>
                """.formatted(apiUrl);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(html);
    }

    // ── Template loader ───────────────────────────────────────────────────────

    private File loadReportTemplate(String fileName) throws IOException {
        if (reportsBasePath.startsWith("/")) {
            // Docker — read directly from mounted volume outside container
            Path path = Paths.get(reportsBasePath, fileName);
            if (!Files.exists(path)) {
                throw new FileNotFoundException(
                        "Report template not found at: " + path);
            }
            return path.toFile();
        }

        // Dev/IDE — read from classpath using getFile() (works outside JAR)
        String resourcePath = reportsBasePath + "/" + fileName;
        ClassPathResource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new FileNotFoundException(
                    "Report template not found on classpath: " + resourcePath);
        }
        return resource.getFile();
    }

    // ── Report builder ───────────────────────────────────────────────────────

    private StiReport buildReport(String phaseNoFrom,
                                  String phaseNoTo,
                                  String companyCode) throws Exception {
        // 1. Resolve company
        String raw = (companyCode == null || companyCode.isBlank())
                ? TenantContext.getCompanyCode() : companyCode;
        String code = (raw != null)
                ? raw.trim().toUpperCase().replace("-", "_") : null;

        AppCompany company = unconstrainedDataManager.load(AppCompany.class)
                .query("select c from AppCompany c where c.companyCode = :code")
                .parameter("code", code)
                .optional()
                .orElseThrow(() -> new IllegalStateException("Company not found: " + code));

        // 2. Build connection string
        String dbConn = "Server="   + company.getDbHost()
                + ";Port="     + company.getDbPort()
                + ";Database=" + company.getDbName()
                + ";User Id="  + company.getDbUsername()
                + ";Password=" + company.getDbPasswordEnc() + ";";

        // 3. Load template
        File reportFile = loadReportTemplate("PrintPhaseList.mrt");
        StiReport report = StiSerializeManager.deserializeReport(reportFile);

        // 4. Set connection
        report.getDictionary().getDatabases().clear();
        report.getDictionary().getDatabases().add(
                new StiPostgreSQLDatabase("pms", dbConn));

        // 5. Set variables
        report.setVariable("SystemName",  systemName);
        report.setVariable("CompanyName", company.getCompanyName());
        report.setVariable("UserId",      currentAuthentication.getUser().getUsername());
        report.setVariable("PhaseNoFrom", phaseNoFrom);
        report.setVariable("PhaseNoTo",   phaseNoTo);

        // 6. Render
        report.render();

        return report;
    }
}