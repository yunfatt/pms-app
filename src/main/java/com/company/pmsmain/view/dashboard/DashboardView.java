package com.company.pmsmain.view.dashboard;

import com.company.pmsmain.entity.Phase;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Overview Dashboard — home page after login.
 *
 * Shows:
 *  - KPI cards: total phases, active phases, unique areas, unique districts
 *  - Bar chart: phases per area
 *  - Pie chart: phases per district
 *  - Recent phases grid
 *
 * Set as home page in application.properties:
 *   jmix.ui.main-view-id=MainView
 *   jmix.flowui.default-view=dashboard
 */
@Route(value = "dashboard", layout = MainView.class)
@ViewController(id = "Dashboard.view")
@ViewDescriptor(path = "dashboard-view.xml")
public class DashboardView extends StandardView {

    // ── Injected XML components ──────────────────────────────────────────────
    @ViewComponent private Span            welcomeLabel;
    @ViewComponent private Span            dateLabel;
    @ViewComponent private VerticalLayout  cardTotalPhases;
    @ViewComponent private VerticalLayout  cardActivePhases;
    @ViewComponent private VerticalLayout  cardTotalAreas;
    @ViewComponent private VerticalLayout  cardTotalDistricts;
    @ViewComponent private VerticalLayout  chartByAreaBox;
    @ViewComponent private VerticalLayout  chartByDistrictBox;
    @ViewComponent private DataGrid<Phase> recentPhasesGrid;

    @Autowired private DataManager          dataManager;
    @Autowired private CurrentAuthentication currentAuthentication;

    // ── Lifecycle ────────────────────────────────────────────────────────────

    @Subscribe
    public void onInit(InitEvent event) {
        renderHeader();
        List<Phase> phases = loadPhases();
        renderKpiCards(phases);
        renderCharts(phases);
        renderRecentGrid(phases);
        injectStyles();
    }

    // ── Header ───────────────────────────────────────────────────────────────

    private void renderHeader() {
        String username = currentAuthentication.getUser().getUsername();
        welcomeLabel.setText("Welcome, " + username);
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")));
    }

    // ── Data loading ─────────────────────────────────────────────────────────

    private List<Phase> loadPhases() {
        try {
            return dataManager.load(Phase.class)
                    .query("select p from Phase p order by p.phaseno")
                    .list();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // ── KPI Cards ────────────────────────────────────────────────────────────

    private void renderKpiCards(List<Phase> phases) {
        long total    = phases.size();
        long active   = phases.stream()
                .filter(p -> p.getDateend() == null
                        || !p.getDateend().before(new Date()))
                .count();
        long areas     = phases.stream()
                .map(Phase::getArea)
                .filter(Objects::nonNull)
                .distinct().count();
        long districts = phases.stream()
                .map(Phase::getDistrict)
                .filter(Objects::nonNull)
                .distinct().count();

        buildKpiCard(cardTotalPhases,    "Total Phases",    String.valueOf(total),    "#1976d2");
        buildKpiCard(cardActivePhases,   "Active Phases",   String.valueOf(active),   "#388e3c");
        buildKpiCard(cardTotalAreas,     "Areas",           String.valueOf(areas),    "#f57c00");
        buildKpiCard(cardTotalDistricts, "Districts",       String.valueOf(districts),"#7b1fa2");
    }

    private void buildKpiCard(VerticalLayout card, String title, String value, String color) {
        card.removeAll();
        card.setWidth("100%");

        Span titleSpan = new Span(title);
        titleSpan.getStyle()
                .set("font-size", "13px")
                .set("color", "#666")
                .set("font-weight", "500");

        Span valueSpan = new Span(value);
        valueSpan.getStyle()
                .set("font-size", "36px")
                .set("font-weight", "bold")
                .set("color", color)
                .set("line-height", "1.2");

        Div colorBar = new Div();
        colorBar.getStyle()
                .set("width", "100%")
                .set("height", "4px")
                .set("background", color)
                .set("border-radius", "2px")
                .set("margin-top", "8px");

        card.add(titleSpan, valueSpan, colorBar);
        card.getStyle()
                .set("background", "#fff")
                .set("border-radius", "8px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("padding", "16px")
                .set("min-width", "140px");
    }

    // ── Charts (ApexCharts via JS) ────────────────────────────────────────────

    private void renderCharts(List<Phase> phases) {
        // --- Bar chart: phases per area ---
        Map<String, Long> byArea = phases.stream()
                .filter(p -> p.getArea() != null && !p.getArea().isBlank())
                .collect(Collectors.groupingBy(Phase::getArea, Collectors.counting()));

        String areaCategories = byArea.keySet().stream()
                .map(k -> "\"" + k + "\"")
                .collect(Collectors.joining(","));
        String areaValues = byArea.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        Div barChart = new Div();
        barChart.setId("chartByArea");
        barChart.setWidthFull();
        barChart.setHeight("300px");

        Span barTitle = new Span("Phases by Area");
        barTitle.getStyle().set("font-weight", "600").set("font-size", "15px");

        chartByAreaBox.removeAll();
        chartByAreaBox.add(barTitle, barChart);
        chartByAreaBox.getStyle()
                .set("background", "#fff")
                .set("border-radius", "8px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("padding", "16px");

        // --- Pie chart: phases per district ---
        Map<String, Long> byDistrict = phases.stream()
                .filter(p -> p.getDistrict() != null && !p.getDistrict().isBlank())
                .collect(Collectors.groupingBy(Phase::getDistrict, Collectors.counting()));

        String districtLabels = byDistrict.keySet().stream()
                .map(k -> "\"" + k + "\"")
                .collect(Collectors.joining(","));
        String districtValues = byDistrict.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        Div pieChart = new Div();
        pieChart.setId("chartByDistrict");
        pieChart.setWidthFull();
        pieChart.setHeight("300px");

        Span pieTitle = new Span("Phases by District");
        pieTitle.getStyle().set("font-weight", "600").set("font-size", "15px");

        chartByDistrictBox.removeAll();
        chartByDistrictBox.add(pieTitle, pieChart);
        chartByDistrictBox.getStyle()
                .set("background", "#fff")
                .set("border-radius", "8px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("padding", "16px");

        // Inject ApexCharts from CDN and render both charts
        String js = """
            const apexScript = document.createElement('script');
            apexScript.src = 'https://cdn.jsdelivr.net/npm/apexcharts';
            apexScript.onload = function() {

                // Bar chart — phases by area
                new ApexCharts(document.getElementById('chartByArea'), {
                    chart: { type: 'bar', height: 280, toolbar: { show: false } },
                    series: [{ name: 'Phases', data: [%s] }],
                    xaxis: { categories: [%s] },
                    colors: ['#1976d2'],
                    plotOptions: { bar: { borderRadius: 4, columnWidth: '55%%' } },
                    dataLabels: { enabled: false },
                    grid: { borderColor: '#f0f0f0' }
                }).render();

                // Pie chart — phases by district
                new ApexCharts(document.getElementById('chartByDistrict'), {
                    chart: { type: 'donut', height: 280 },
                    series: [%s],
                    labels: [%s],
                    colors: ['#1976d2','#388e3c','#f57c00','#7b1fa2','#0097a7','#e53935'],
                    legend: { position: 'bottom' },
                    dataLabels: { enabled: true }
                }).render();
            };
            document.head.appendChild(apexScript);
            """.formatted(areaValues, areaCategories, districtValues, districtLabels);

        getUI().ifPresent(ui -> ui.getPage().executeJs(js));
    }

    // ── Recent phases grid ───────────────────────────────────────────────────

    private void renderRecentGrid(List<Phase> phases) {
        // Show the 20 most recently added phases (last by phaseno sort)
        List<Phase> recent = phases.stream()
                .sorted(Comparator.comparing(Phase::getPhaseno, Comparator.reverseOrder()))
                .limit(20)
                .collect(Collectors.toList());
        recentPhasesGrid.setItems(recent);
    }

    // ── Global styles ────────────────────────────────────────────────────────

    private void injectStyles() {
        getUI().ifPresent(ui -> ui.getPage().executeJs("""
            const style = document.createElement('style');
            style.textContent = `
                .dashboard-welcome {
                    font-size: 20px;
                    font-weight: 600;
                    flex: 1;
                }
                .dashboard-date {
                    font-size: 13px;
                    color: #888;
                }
                .kpi-card {
                    flex: 1;
                }
                .chart-box {
                    background: #fff;
                    border-radius: 8px;
                }
                .section-title {
                    font-size: 15px;
                    font-weight: 600;
                    margin-bottom: 8px;
                    display: block;
                }
            `;
            document.head.appendChild(style);
            """));
    }
}
