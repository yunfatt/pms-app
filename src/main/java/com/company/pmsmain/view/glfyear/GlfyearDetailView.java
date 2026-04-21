package com.company.pmsmain.view.glfyear;

import com.company.pmsmain.entity.Glfyear;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Route(value = "glfyears/:id", layout = MainView.class)
@ViewController(id = "Glfyear.detail")
@ViewDescriptor(path = "glfyear-detail-view.xml")
@EditedEntityContainer("glfyearDc")
public class GlfyearDetailView extends StandardDetailView<Glfyear> {

    @Autowired private DataManager dataManager;
    @Autowired private Notifications notifications;

    @ViewComponent private IntegerField accperiodField;
    @ViewComponent private InstanceContainer<Glfyear> glfyearDc;

    @ViewComponent private Checkbox pctrl1;
    @ViewComponent private Checkbox pctrl2;
    @ViewComponent private Checkbox pctrl3;
    @ViewComponent private Checkbox pctrl4;
    @ViewComponent private Checkbox pctrl5;
    @ViewComponent private Checkbox pctrl6;
    @ViewComponent private Checkbox pctrl7;
    @ViewComponent private Checkbox pctrl8;
    @ViewComponent private Checkbox pctrl9;
    @ViewComponent private Checkbox pctrl10;
    @ViewComponent private Checkbox pctrl11;
    @ViewComponent private Checkbox pctrl12;
    @ViewComponent private Checkbox pctrl13;
    @ViewComponent private Checkbox pctrl14;
    @ViewComponent private Checkbox pctrl15;
    @ViewComponent private Checkbox pctrl16;
    @ViewComponent private Checkbox pctrl17;
    @ViewComponent private Checkbox pctrl18;

    private List<Checkbox> periodCheckboxes() {
        return List.of(pctrl1, pctrl2, pctrl3, pctrl4, pctrl5, pctrl6,
                pctrl7, pctrl8, pctrl9, pctrl10, pctrl11, pctrl12,
                pctrl13, pctrl14, pctrl15, pctrl16, pctrl17, pctrl18);
    }

    @Subscribe
    public void onReady(ReadyEvent event) {
        refreshPeriods();
    }

    @Subscribe(id = "glfyearDc", target = Target.DATA_CONTAINER)
    public void onGlfyearDcItemPropertyChange(
            InstanceContainer.ItemPropertyChangeEvent<Glfyear> event) {
        if ("initdate".equals(event.getProperty()) || "finaldate".equals(event.getProperty())) {
            refreshPeriods();
        }
    }

    private void refreshPeriods() {
        Glfyear entity = glfyearDc.getItem();
        int activePeriods = calculateActivePeriods(entity);

        accperiodField.setValue(activePeriods > 0 ? activePeriods : null);
        entity.setAccperiod(activePeriods > 0 ? (short) activePeriods : null);

        List<Checkbox> boxes = periodCheckboxes();
        for (int i = 0; i < boxes.size(); i++) {
            boolean withinRange = (i + 1) <= activePeriods;
            boxes.get(i).setEnabled(withinRange);
            if (!withinRange) boxes.get(i).setValue(false);
        }
    }

    private int calculateActivePeriods(Glfyear entity) {
        if (entity.getInitdate() == null || entity.getFinaldate() == null) return 0;

        LocalDate start = toLocalDate(entity.getInitdate());
        LocalDate end   = toLocalDate(entity.getFinaldate());

        if (end.isBefore(start)) return 0;

        int months = (int) ChronoUnit.MONTHS.between(
                start.withDayOfMonth(1), end.withDayOfMonth(1)) + 1;
        return Math.max(0, Math.min(months, 18));
    }

    private LocalDate toLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Subscribe
    public void onBeforeSave(StandardDetailView.BeforeSaveEvent event) {
        Glfyear entity = glfyearDc.getItem();
        int activePeriods = calculateActivePeriods(entity);

        entity.setAccperiod(activePeriods > 0 ? (short) activePeriods : null);

        List<Checkbox> boxes = periodCheckboxes();
        for (int i = activePeriods; i < boxes.size(); i++) {
            boxes.get(i).setValue(false);
        }

        if (!validateDateRange(entity)) {
            event.preventSave();
        }
    }

    private boolean validateDateRange(Glfyear entity) {
        if (entity.getInitdate() == null || entity.getFinaldate() == null) return true;

        LocalDate newStart = toLocalDate(entity.getInitdate());
        LocalDate newEnd   = toLocalDate(entity.getFinaldate());
        String currentFiscalyear = entity.getId().getFiscalyear();
        String currentCompno     = entity.getId().getCompno();

        List<Glfyear> existing = dataManager.load(Glfyear.class)
                .query("select g from Glfyear g " +
                        "where g.id.compno = :compno " +
                        "and g.id.fiscalyear <> :fiscalyear")
                .parameter("compno", currentCompno)
                .parameter("fiscalyear", currentFiscalyear)
                .list();

        for (Glfyear other : existing) {
            if (other.getInitdate() == null || other.getFinaldate() == null) continue;
            LocalDate otherStart = toLocalDate(other.getInitdate());
            LocalDate otherEnd   = toLocalDate(other.getFinaldate());
            if (!newStart.isAfter(otherEnd) && !newEnd.isBefore(otherStart)) {
                notifications.show("Overlap with fiscal year " + other.getFiscalyear()
                        + " (" + otherStart + " – " + otherEnd + ")");
                return false;
            }
        }

        Glfyear prevYear = existing.stream()
                .filter(g -> g.getFinaldate() != null && toLocalDate(g.getFinaldate()).isBefore(newStart))
                .max(Comparator.comparing(g -> toLocalDate(g.getFinaldate())))
                .orElse(null);

        Glfyear nextYear = existing.stream()
                .filter(g -> g.getInitdate() != null && toLocalDate(g.getInitdate()).isAfter(newEnd))
                .min(Comparator.comparing(g -> toLocalDate(g.getInitdate())))
                .orElse(null);

        if (prevYear != null) {
            LocalDate prevEnd = toLocalDate(prevYear.getFinaldate());
            if (ChronoUnit.DAYS.between(prevEnd, newStart) > 1) {
                notifications.show("Gap detected after fiscal year " + prevYear.getFiscalyear()
                        + " (ends " + prevEnd + ", this starts " + newStart + ")");
                return false;
            }
        }

        if (nextYear != null) {
            LocalDate nextStart = toLocalDate(nextYear.getInitdate());
            if (ChronoUnit.DAYS.between(newEnd, nextStart) > 1) {
                notifications.show("Gap detected before fiscal year " + nextYear.getFiscalyear()
                        + " (starts " + nextStart + ", this ends " + newEnd + ")");
                return false;
            }
        }

        return true;
    }
}
