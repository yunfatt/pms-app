package com.company.pmsmain.view.shared.lookup;

import com.company.pmsmain.entity.enums.CountryCode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.textfield.TypedTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("pmsmain_CountryCodeLookupBean")
public class CountryCodeLookupBean {

    @Autowired
    private UiComponents uiComponents;

    public void open(TypedTextField<String> target) {
        CountryCode[] values = CountryCode.values();

        Dialog dialog = buildDialog();

        TextField search = uiComponents.create(TextField.class);
        search.setPlaceholder("Search by code or country…");
        search.setWidthFull();

        Grid<CountryCode> grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeight("420px");
        grid.addColumn(CountryCode::getCode)
                .setHeader("Code")
                .setWidth("90px")
                .setFlexGrow(0);
        grid.addColumn(CountryCode::getCountry)
                .setHeader("Country")
                .setFlexGrow(1);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(Arrays.asList(values));

        String current = target.getTypedValue();
        if (current != null) {
            Arrays.stream(values)
                    .filter(c -> c.getCode().equals(current))
                    .findFirst()
                    .ifPresent(grid::select);
        }

        Button selectBtn = primaryButton("Select");
        selectBtn.setEnabled(current != null);

        search.addValueChangeListener(e -> {
            String term = e.getValue().trim().toLowerCase();
            List<CountryCode> filtered = Arrays.stream(values)
                    .filter(c -> term.isEmpty()
                            || c.getCode().toLowerCase().contains(term)
                            || c.getCountry().toLowerCase().contains(term))
                    .collect(Collectors.toList());
            grid.setItems(filtered);
            selectBtn.setEnabled(grid.getSelectedItems().stream().findFirst().isPresent());
        });

        grid.addSelectionListener(e ->
                selectBtn.setEnabled(e.getFirstSelectedItem().isPresent()));

        grid.addItemDoubleClickListener(e -> {
            target.setTypedValue(e.getItem().getCode());
            dialog.close();
        });

        selectBtn.addClickListener(e -> {
            grid.getSelectedItems().stream().findFirst()
                    .ifPresent(c -> target.setTypedValue(c.getCode()));
            dialog.close();
        });

        Button cancelBtn = uiComponents.create(Button.class);
        cancelBtn.setText("Cancel");
        cancelBtn.addClickListener(e -> dialog.close());

        VerticalLayout content = new VerticalLayout(search, grid);
        content.setPadding(false);
        content.setSpacing(true);

        dialog.add(content);
        dialog.getFooter().add(cancelBtn, selectBtn);
        dialog.open();

        dialog.addOpenedChangeListener(ev -> {
            if (ev.isOpened()) search.focus();
        });
    }

    private Dialog buildDialog() {
        Dialog d = new Dialog();
        d.setHeaderTitle("Select country");
        d.setWidth("460px");
        d.setCloseOnOutsideClick(true);
        return d;
    }

    private Button primaryButton(String label) {
        Button b = uiComponents.create(Button.class);
        b.setText(label);
        b.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return b;
    }
}