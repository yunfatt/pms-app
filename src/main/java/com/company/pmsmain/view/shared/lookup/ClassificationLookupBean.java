package com.company.pmsmain.view.shared.lookup;

import com.company.pmsmain.entity.enums.ClassificationCode;
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
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("pmsmain_ClassificationLookupBean")
public class ClassificationLookupBean {

    @Autowired
    private UiComponents uiComponents;

    public void open(TypedTextField<String> target) {
        ClassificationCode[] values = ClassificationCode.values();

        Dialog dialog = buildDialog();

        TextField search = uiComponents.create(TextField.class);
        search.setPlaceholder("Search by code or description…");
        search.setWidthFull();

        Grid<ClassificationCode> grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeight("360px");
        grid.addColumn(ClassificationCode::getCode)
                .setHeader("Code")
                .setWidth("90px")
                .setFlexGrow(0);
        grid.addColumn(ClassificationCode::getDescription)
                .setHeader("Description")
                .setFlexGrow(1);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(Arrays.asList(values));

        // Pre-select if field already has a value
        String current = target.getTypedValue();
        if (current != null) {
            Arrays.stream(values)
                    .filter(cc -> cc.getCode().equals(current))
                    .findFirst()
                    .ifPresent(grid::select);
        }

        // Double-click to confirm immediately
        grid.addItemDoubleClickListener(e -> {
            target.setTypedValue(e.getItem().getCode());
            dialog.close();
        });

        Button selectBtn = primaryButton("Select");
        selectBtn.setEnabled(current != null);

        search.addValueChangeListener(e -> {
            String term = e.getValue().trim().toLowerCase();
            List<ClassificationCode> filtered = Arrays.stream(values)
                    .filter(cc -> term.isEmpty()
                            || cc.getCode().contains(term)
                            || cc.getDescription().toLowerCase().contains(term))
                    .collect(Collectors.toList());
            grid.setItems(filtered);
            selectBtn.setEnabled(
                    grid.getSelectedItems().stream().findFirst().isPresent()
            );
        });

        grid.addSelectionListener(e ->
                selectBtn.setEnabled(e.getFirstSelectedItem().isPresent()));

        selectBtn.addClickListener(e -> {
            grid.getSelectedItems().stream().findFirst()
                    .ifPresent(cc -> target.setTypedValue(cc.getCode()));
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

        // Focus search after open
        dialog.addOpenedChangeListener(ev -> {
            if (ev.isOpened()) search.focus();
        });
    }

    private Dialog buildDialog() {
        Dialog d = new Dialog();
        d.setHeaderTitle("Select classification");
        d.setWidth("560px");
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