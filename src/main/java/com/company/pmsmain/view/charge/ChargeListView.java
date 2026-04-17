package com.company.pmsmain.view.charge;

import com.company.pmsmain.entity.Charge;
import com.company.pmsmain.view.BaseListView;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.genericfilter.GenericFilter;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import com.company.pmsmain.entity.PeriodEnum;
import com.vaadin.flow.component.combobox.ComboBox;

@Route(value = "charges", layout = MainView.class)
@ViewController(id = "Charge.list")
@ViewDescriptor(path = "charge-list-view.xml")
@LookupComponent("chargesDataGrid")
@DialogMode(width = "64em")
public class ChargeListView extends BaseListView<Charge> {

    @ViewComponent
    private TypedTextField<String> searchField;

    @ViewComponent
    private JmixButton searchButton;

    @ViewComponent
    private JmixButton advancedSearchToggle;

    @ViewComponent
    private GenericFilter genericFilter;

    @ViewComponent
    private CollectionLoader<Charge> chargesDl;

    private boolean advancedMode = false;

    private ComboBox<PeriodEnum> periodFilter;
    private PeriodEnum selectedPeriod = null;
    @Subscribe
    public void onInit(InitEvent event) {
        setupSearch();
        setupPeriodFilter();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        chargesDl.setParameter("searchText", "");
        chargesDl.setParameter("period", (short) 0);
        chargesDl.load();
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
            advancedSearchToggle.setIcon(
                    com.vaadin.flow.component.icon.VaadinIcon.SEARCH.create());
        } else {
            advancedSearchToggle.setText("Advanced");
            advancedSearchToggle.setIcon(
                    com.vaadin.flow.component.icon.VaadinIcon.FILTER.create());
        }

        searchField.setVisible(!advancedMode);
        searchButton.setVisible(!advancedMode);
        genericFilter.setVisible(advancedMode);

        if (!advancedMode) {
            chargesDl.setParameter("searchText", "");
            chargesDl.load();
        } else {
            chargesDl.removeParameter("searchText");
        }
    }

    private void setupSearch() {
        searchField.addKeyPressListener(Key.ENTER, e -> executeSearch());
        searchField.addValueChangeListener(e -> {
            if (e.getValue() == null || e.getValue().isBlank()) {
                executeSearch();
            }
        });
    }

    private void executeSearch() {
        String text = searchField.getValue();
        chargesDl.setParameter("searchText",
                text == null ? "" : text.trim());
        chargesDl.setParameter("period",
                selectedPeriod != null ? selectedPeriod.toShort() : (short) 0);

        System.out.println(">>> Filter period: " +
                (selectedPeriod != null ? selectedPeriod.toShort() : "all"));

        chargesDl.load();
    }
    private void setupPeriodFilter() {
        periodFilter = new ComboBox<>();
        periodFilter.setItems(PeriodEnum.values());
        periodFilter.setItemLabelGenerator(PeriodEnum::getLabel);
        periodFilter.setPlaceholder("All Periods");
        periodFilter.setClearButtonVisible(true);
        periodFilter.setWidth("240px");

        periodFilter.addValueChangeListener(e -> {
            selectedPeriod = e.getValue();
            executeSearch();
        });

        // Find the searchBar hbox from content and add the combobox
        getContent().getChildren()
                .filter(c -> c instanceof HorizontalLayout)
                .map(c -> (HorizontalLayout) c)
                .findFirst()
                .ifPresent(hbox -> hbox.add(periodFilter));
    }
}