package com.company.pmsmain.view.ardoc;

import com.company.pmsmain.entity.Ardoc;
import com.company.pmsmain.view.main.MainView;
import com.company.pmsmain.view.shared.lookup.ClassificationLookupBean;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.EntityStates;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Route(value = "ardocs/:id", layout = MainView.class)
@ViewController(id = "Ardoc.detail")
@ViewDescriptor(path = "ardoc-detail-view.xml")
@EditedEntityContainer("ardocDc")
public class ArdocDetailView extends StandardDetailView<Ardoc> {

    // ── Dependencies ──────────────────────────────────────────────

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private ClassificationLookupBean classificationLookupBean;

    // ── Field bindings ────────────────────────────────────────────

    @ViewComponent
    private TypedTextField<String> refundcodeField;

    @ViewComponent
    private TypedTextField<String> apaccnoField;

    @ViewComponent
    private TypedTextField<String> classificationField;

    @ViewComponent
    private TypedTextField<String> trxncodeField;

    @Autowired
    private EntityStates entityStates;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        trxncodeField.setReadOnly(!entityStates.isNew(getEditedEntity()));
    }
    // ── Classification ────────────────────────────────────────────

    @Subscribe("lookupClassificationBtn")
    public void onLookupClassificationBtnClick(ClickEvent<Button> event) {
        classificationLookupBean.open(classificationField);
    }

    // ── Refund Code ───────────────────────────────────────────────

    @Subscribe("lookupRefundCodeBtn")
    public void onLookupRefundCodeBtnClick(ClickEvent<Button> event) {
        List<Ardoc> all = dataManager.load(Ardoc.class)
                .query("select e from Ardoc e where e.ttype = :ttype and e.ptype = :ptype")
                .parameter("ttype", "B")
                .parameter("ptype", "R")
                .list();

        openEntityDialog(
                "Select refund code",
                all,
                new ColDef<>("Code",        Ardoc::getTrxncode,    "110px", 0),
                new ColDef<>("Description", Ardoc::getDescription, null,    1),
                Ardoc::getTrxncode,
                refundcodeField
        );
    }

    // ── AP Billing A/C No. ────────────────────────────────────────

    @Subscribe("lookupApAccNoBtn")
    public void onLookupApAccNoBtnClick(ClickEvent<Button> event) {
        List<Ardoc> all = dataManager.load(Ardoc.class)   // ← replace with actual AP/GL account entity
                .all()
                .list();

        openEntityDialog(
                "Select AP billing account",
                all,
                new ColDef<>("A/C No.",     Ardoc::getTrxncode,    "130px", 0),
                new ColDef<>("Description", Ardoc::getDescription, null,    1),
                Ardoc::getTrxncode,
                apaccnoField
        );
    }

    // ── Generic entity dialog ─────────────────────────────────────

    private <E> void openEntityDialog(
            String title,
            List<E> items,
            ColDef<E> codeCol,
            ColDef<E> descCol,
            Function<E, String> valueFn,
            TypedTextField<String> target
    ) {
        Dialog dialog = buildDialog(title);

        TextField search = uiComponents.create(TextField.class);
        search.setPlaceholder("Search…");
        search.setWidthFull();

        Grid<E> grid = new Grid<>();
        grid.setWidthFull();
        grid.setHeight("340px");
        grid.addColumn(codeCol.getter::apply)
                .setHeader(codeCol.header)
                .setWidth(codeCol.width)
                .setFlexGrow(codeCol.flexGrow);
        grid.addColumn(descCol.getter::apply)
                .setHeader(descCol.header)
                .setFlexGrow(descCol.flexGrow);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(items);

        String current = target.getTypedValue();
        if (current != null) {
            items.stream()
                    .filter(e -> current.equals(valueFn.apply(e)))
                    .findFirst()
                    .ifPresent(grid::select);
        }

        Button selectBtn = primaryButton("Select");
        selectBtn.setEnabled(current != null);

        search.addValueChangeListener(e -> {
            String term = e.getValue().trim().toLowerCase();
            grid.setItems(items.stream()
                    .filter(en -> {
                        if (term.isEmpty()) return true;
                        String code = codeCol.getter.apply(en);
                        String desc = descCol.getter.apply(en);
                        return (code != null && code.toLowerCase().contains(term))
                                || (desc != null && desc.toLowerCase().contains(term));
                    })
                    .collect(Collectors.toList()));
            selectBtn.setEnabled(grid.getSelectedItems().stream().findFirst().isPresent());
        });

        grid.addSelectionListener(e ->
                selectBtn.setEnabled(e.getFirstSelectedItem().isPresent()));

        grid.addItemDoubleClickListener(e -> {
            target.setTypedValue(valueFn.apply(e.getItem()));
            dialog.close();
        });

        selectBtn.addClickListener(e -> {
            grid.getSelectedItems().stream().findFirst()
                    .ifPresent(en -> target.setTypedValue(valueFn.apply(en)));
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

    // ── Helpers ───────────────────────────────────────────────────

    private Dialog buildDialog(String title) {
        Dialog d = new Dialog();
        d.setHeaderTitle(title);
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

    // ── Column definition ─────────────────────────────────────────

    private static class ColDef<E> {
        final String header;
        final Function<E, String> getter;
        final String width;
        final int flexGrow;

        ColDef(String header, Function<E, String> getter,
               String width, int flexGrow) {
            this.header   = header;
            this.getter   = getter;
            this.width    = width;
            this.flexGrow = flexGrow;
        }
    }
}
