package com.company.pmsmain.view.shared.lookup;

import com.company.pmsmain.entity.enums.MalaysiaState;
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

@Component("pmsmain_MalaysiaStateLookupBean")
public class MalaysiaStateLookupBean {

    @Autowired
    private UiComponents uiComponents;

    public void open(TypedTextField<String> target) {
        MalaysiaState[] values = MalaysiaState.values();

        Dialog dialog = buildDialog();

        TextField search = uiComponents.create(TextField.class);
        search.setPlaceholder("Search by code or state…");
        search.setWidthFull();

        Grid<MalaysiaState> grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeight("400px");
        grid.addColumn(MalaysiaState::getCode)
                .setHeader("Code")
                .setWidth("80px")
                .setFlexGrow(0);
        grid.addColumn(MalaysiaState::getState)
                .setHeader("State")
                .setFlexGrow(1);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(Arrays.asList(values));

        String current = target.getTypedValue();
        if (current != null) {
            Arrays.stream(values)
                    .filter(s -> s.getCode().equals(current))
                    .findFirst()
                    .ifPresent(grid::select);
        }

        Button selectBtn = primaryButton("Select");
        selectBtn.setEnabled(current != null);

        search.addValueChangeListener(e -> {
            String term = e.getValue().trim().toLowerCase();
            List<MalaysiaState> filtered = Arrays.stream(values)
                    .filter(s -> term.isEmpty()
                            || s.getCode().contains(term)
                            || s.getState().toLowerCase().contains(term))
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
                    .ifPresent(s -> target.setTypedValue(s.getCode()));
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
        d.setHeaderTitle("Select state");
        d.setWidth("420px");
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