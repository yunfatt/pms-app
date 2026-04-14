package com.company.pmsmain.view.ardoc;

import com.company.pmsmain.entity.Ardoc;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.View.InitEvent;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

import java.util.Arrays;

@Route(value = "ardocs/:id", layout = MainView.class)
@ViewController(id = "Ardoc.detail")
@ViewDescriptor(path = "ardoc-detail-view.xml")
@EditedEntityContainer("ardocDc")
public class ArdocDetailView extends StandardDetailView<Ardoc> {

    @ViewComponent
    private JmixSelect<Character> ttypeField;

    @ViewComponent
    private JmixSelect<Character> ptypeField;

    @ViewComponent
    private JmixSelect<Character> billtoField;

    @ViewComponent
    private Button saveAndCloseButton;

    @ViewComponent
    private Button lookupRefundCodeBtn;

    @ViewComponent
    private Button lookupApAccNoBtn;

    @ViewComponent
    private Button lookupClassificationBtn;
    @ViewComponent
    private JmixSelect<Short> depositField;
    @ViewComponent
    private JmixSelect<Short> utilityField;

    @ViewComponent
    private JmixSelect<Short> sinkingfundField;

    @ViewComponent
    private JmixSelect<Short> longtermField;

    @ViewComponent
    private JmixSelect<Short> chargeinterestField;

    @ViewComponent
    private TypedTextField<String> trxncodeField;
    @ViewComponent
    private TypedTextField<String> termField;

    @Subscribe
    public void onInit(final InitEvent event) {

        // ✅ Wire up selects — no entity access here
        ttypeField.setItems(Arrays.asList('B', 'P'));
        ttypeField.setItemLabelGenerator(c -> {
            if (c == null) return "";
            return switch (c) {
                case 'B' -> "Billing";
                case 'P' -> "Payment";
                default -> String.valueOf(c);
            };
        });

        billtoField.setItems(Arrays.asList('O', 'T'));
        billtoField.setItemLabelGenerator(c -> {
            if (c == null) return "";
            return switch (c) {
                case 'O' -> "Owner";
                case 'T' -> "Tenant";
                default -> String.valueOf(c);
            };
        });

        // ✅ ttype change listener — entity is available by the time user changes it
        ttypeField.addValueChangeListener(e -> {
            getEditedEntity().setTtype(e.getValue());
            refreshPtypeItems(e.getValue());
            getEditedEntity().setPtype(null);
            ptypeField.setValue(null);
        });

        saveAndCloseButton.addClickListener(e -> closeWithSave());

        lookupRefundCodeBtn.addClickListener(e ->
                Notification.show("Refund Code lookup not yet implemented",
                        2000, Notification.Position.MIDDLE));

        lookupApAccNoBtn.addClickListener(e ->
                Notification.show("AP A/C lookup not yet implemented",
                        2000, Notification.Position.MIDDLE));

        lookupClassificationBtn.addClickListener(e ->
                Notification.show("Classification lookup not yet implemented",
                        2000, Notification.Position.MIDDLE));
        initShortBoolSelect(depositField);
        initShortBoolSelect(utilityField);
        initShortBoolSelect(sinkingfundField);
        initShortBoolSelect(longtermField);
        initShortBoolSelect(chargeinterestField);
    }
    private void initShortBoolSelect(JmixSelect<Short> select) {
        select.setItems(Arrays.asList((short) 0, (short) 1));
        select.setItemLabelGenerator(s -> {
            if (s == null) return "";
            return s != 0 ? "Yes" : "No";
        });
    }
    @Subscribe
    public void onReady(final ReadyEvent event) {
        refreshPtypeItems(getEditedEntity().getTtype());

        Short term = getEditedEntity().getTerm();
        termField.setValue(term == null ? "" : String.valueOf(term));

        boolean isNew = getEditedEntity().getTrxncode() == null
                || getEditedEntity().getTrxncode().isBlank();
        trxncodeField.setReadOnly(!isNew);
    }
    private void refreshPtypeItems(Character ttype) {
        if (ttype == null || ttype == 'B') {
            // Billing type: Billing or Refund
            ptypeField.setItems(Arrays.asList('B', 'R'));
            ptypeField.setItemLabelGenerator(c -> {
                if (c == null) return "";
                return switch (c) {
                    case 'B' -> "Billing";
                    case 'R' -> "Refund";
                    default -> String.valueOf(c);
                };
            });
        } else {
            // Payment type: Payment or Credit Note
            ptypeField.setItems(Arrays.asList('P', 'C'));
            ptypeField.setItemLabelGenerator(c -> {
                if (c == null) return "";
                return switch (c) {
                    case 'P' -> "Payment";
                    case 'C' -> "Credit Note";
                    default -> String.valueOf(c);
                };
            });
        }
    }
}