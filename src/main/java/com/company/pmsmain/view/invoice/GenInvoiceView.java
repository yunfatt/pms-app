package com.company.pmsmain.view.invoice;

import com.company.pmsmain.dto.GenInvoiceParams;
import com.company.pmsmain.dto.InvoiceLineItem;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Combined Generate Invoice view.
 *
 * Tab 1 — Parameters (GenInvoicePrompt):
 *   Collect property range, dates, charges month, tax type, duplicate check.
 *   "Next" validates and unlocks Tab 2.
 *
 * Tab 2 — Charge Lines (GenInvoice ChildList):
 *   Review/edit charge lines loaded by the service.
 *   "Generate" triggers the invoice generation routine.
 *   "Back" returns to Tab 1 to adjust parameters.
 */
@Route(value = "gen-invoice", layout = MainView.class)
@ViewController(id = "GenInvoice.view")
@ViewDescriptor(path = "gen-invoice-view.xml")
public class GenInvoiceView extends StandardView {

    // ── Tab 1: Parameters ────────────────────────────────────────────────────
    @ViewComponent private TextField  lastInvNoField;
    @ViewComponent private TextField  propertyNo1Field;
    @ViewComponent private TextField  propertyNo2Field;
    @ViewComponent private DatePicker invoiceDateField;
    @ViewComponent private Select<String>  chargesMonthField;
    @ViewComponent private DatePicker generatePeriodFromField;
    @ViewComponent private DatePicker generatePeriodToField;
    @ViewComponent private Select<String>  taxInvTypeField;
    @ViewComponent private Checkbox   checkDuplicateField;
    @ViewComponent private JmixButton propertyNo1LookupBtn;
    @ViewComponent private JmixButton propertyNo2LookupBtn;
    @ViewComponent private JmixButton nextButton;
    @ViewComponent private JmixButton cancelButton;

    // ── Tab 2: Charge Lines ──────────────────────────────────────────────────
    @ViewComponent private TextField  chargeNoField;
    @ViewComponent private JmixButton chargeNoLookupBtn;
    @ViewComponent private TextField  descriptionField;
    @ViewComponent private DatePicker lineDateFromField;
    @ViewComponent private DatePicker lineDateToField;
    @ViewComponent private DatePicker lineDueDateField;
    @ViewComponent private TextField  qtyField;
    @ViewComponent private TextField  priceField;
    @ViewComponent private TextField  qty2Field;
    @ViewComponent private TextField  price2Field;
    @ViewComponent private TextField  qty3Field;
    @ViewComponent private TextField  price3Field;
    @ViewComponent private JmixButton insertLineBtn;
    @ViewComponent private JmixButton deleteLineBtn;
    @ViewComponent private JmixButton backButton;
    @ViewComponent private JmixButton generateButton;
    @ViewComponent private JmixButton cancelButton2;
    @ViewComponent private VerticalLayout lineItemsGridPlaceholder;

    // ── Tab references ───────────────────────────────────────────────────────
    @ViewComponent private Tab parametersTab;
    @ViewComponent private Tab chargeLinesTab;

    @Autowired private Notifications notifications;

    /** Grid built programmatically — InvoiceLineItem is a DTO, not a Jmix entity */
    private Grid<InvoiceLineItem> lineItemsGrid;

    /** In-memory charge lines — mirrors Clarion ChildList queue */
    private final List<InvoiceLineItem> lineItems = new ArrayList<>();

    // ────────────────────────────────────────────────────────────────────────
    // Lifecycle
    // ────────────────────────────────────────────────────────────────────────

    @Subscribe
    public void onInit(InitEvent event) {
        initDefaults();
        configureGrid();
        wireButtons();
    }

    private void initDefaults() {
        // Default invoice date → today
        invoiceDateField.setValue(LocalDate.now());

        // Default generate period → current month
        LocalDate first = LocalDate.now().withDayOfMonth(1);
        LocalDate last  = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        generatePeriodFromField.setValue(first);
        generatePeriodToField.setValue(last);

        // Default charges month → current month
        chargesMonthField.setValue(String.valueOf(LocalDate.now().getMonthValue()));

        // Default tax inv type → Exclusive
        taxInvTypeField.setValue("3");
    }

    private void configureGrid() {
        // Build Vaadin Grid programmatically — InvoiceLineItem is a DTO, not a Jmix entity
        // Jmix dataGrid XML requires dataContainer/metaClass, so we build it in Java
        lineItemsGrid = new Grid<>(InvoiceLineItem.class, false);
        lineItemsGrid.setWidthFull();
        lineItemsGrid.setMinHeight("16em");
        lineItemsGrid.setColumnReorderingAllowed(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getChargeNo)   .setHeader("Code")       .setWidth("8em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getDescription).setHeader("Description").setWidth("20em").setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getDateFrom)   .setHeader("From")       .setWidth("9em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getDateTo)     .setHeader("To")         .setWidth("9em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getDueDate)    .setHeader("Due Date")   .setWidth("9em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getQty)        .setHeader("Qty")        .setWidth("7em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getPrice)      .setHeader("Price")      .setWidth("9em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getQty2)       .setHeader("Qty 2")      .setWidth("7em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getPrice2)     .setHeader("Price 2")    .setWidth("9em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getQty3)       .setHeader("Qty 3")      .setWidth("7em") .setResizable(true);
        lineItemsGrid.addColumn(InvoiceLineItem::getPrice3)     .setHeader("Price 3")    .setWidth("9em") .setResizable(true);
        lineItemsGrid.setItems(lineItems);
        lineItemsGrid.addSelectionListener(e ->
                e.getFirstSelectedItem().ifPresent(this::populateEditorFromLine));

        // Inject into the vbox placeholder defined in XML
        if (lineItemsGridPlaceholder != null) {
            lineItemsGridPlaceholder.add(lineItemsGrid);
            lineItemsGrid.setWidthFull();
        }
    }

    private void wireButtons() {
        // Tab 1
        propertyNo1LookupBtn.addClickListener(e -> openPropertyLookup(propertyNo1Field));
        propertyNo2LookupBtn.addClickListener(e -> openPropertyLookup(propertyNo2Field));
        nextButton.addClickListener(e -> onNext());
        cancelButton.addClickListener(e -> close(StandardOutcome.DISCARD));

        // Tab 2
        chargeNoLookupBtn.addClickListener(e -> openChargeLookup());
        insertLineBtn.addClickListener(e -> insertLine());
        deleteLineBtn.addClickListener(e -> deleteLine());
        backButton.addClickListener(e -> goToTab1());
        generateButton.addClickListener(e -> onGenerate());
        cancelButton2.addClickListener(e -> close(StandardOutcome.DISCARD));
    }

    // ────────────────────────────────────────────────────────────────────────
    // Tab 1 — Next: validate parameters and move to Tab 2
    // ────────────────────────────────────────────────────────────────────────

    private void onNext() {
        if (!validateParameters()) return;

        // Build params from Tab 1 fields
        GenInvoiceParams params = buildParams();

        // TODO: call GenInvoiceService.loadLineItems(params) and populate grid
        // For now just unlock Tab 2 and switch to it
        // Example:
        // lineItems.clear();
        // lineItems.addAll(genInvoiceService.loadLineItems(params));
        // lineItemsGrid.getDataProvider().refreshAll();

        // Unlock and navigate to Tab 2
        chargeLinesTab.setEnabled(true);
        goToTab2();
    }

    // ────────────────────────────────────────────────────────────────────────
    // Tab 2 — Generate invoice
    // ────────────────────────────────────────────────────────────────────────

    private void onGenerate() {
        if (lineItems.isEmpty()) {
            notifications.create("No charge lines to generate.")
                    .withType(Notifications.Type.WARNING)
                    .show();
            return;
        }

        GenInvoiceParams params = buildParams();

        // TODO: call GenInvoiceService.generateInvoices(params, lineItems)
        // Example:
        // long lastInvNo = genInvoiceService.generateInvoices(params, lineItems);
        // lastInvNoField.setValue(String.valueOf(lastInvNo));

        notifications.create("Invoice generation not yet implemented.")
                .withType(Notifications.Type.WARNING)
                .show();
    }

    // ────────────────────────────────────────────────────────────────────────
    // Line item editor helpers
    // ────────────────────────────────────────────────────────────────────────

    private void populateEditorFromLine(InvoiceLineItem item) {
        chargeNoField.setValue(nullSafe(item.getChargeNo()));
        descriptionField.setValue(nullSafe(item.getDescription()));
        lineDateFromField.setValue(item.getDateFrom());
        lineDateToField.setValue(item.getDateTo());
        lineDueDateField.setValue(item.getDueDate());
        qtyField.setValue(decStr(item.getQty()));
        priceField.setValue(decStr(item.getPrice()));
        qty2Field.setValue(decStr(item.getQty2()));
        price2Field.setValue(decStr(item.getPrice2()));
        qty3Field.setValue(decStr(item.getQty3()));
        price3Field.setValue(decStr(item.getPrice3()));
    }

    private void insertLine() {
        InvoiceLineItem item = new InvoiceLineItem();
        item.setChargeNo(chargeNoField.getValue());
        item.setDescription(descriptionField.getValue());
        item.setDateFrom(lineDateFromField.getValue());
        item.setDateTo(lineDateToField.getValue());
        item.setDueDate(lineDueDateField.getValue());
        item.setQty(parseBD(qtyField.getValue()));
        item.setPrice(parseBD(priceField.getValue()));
        item.setQty2(parseBD(qty2Field.getValue()));
        item.setPrice2(parseBD(price2Field.getValue()));
        item.setQty3(parseBD(qty3Field.getValue()));
        item.setPrice3(parseBD(price3Field.getValue()));
        lineItems.add(item);
        lineItemsGrid.getDataProvider().refreshAll();
        clearEditor();
    }

    private void deleteLine() {
        lineItemsGrid.getSelectedItems().forEach(lineItems::remove);
        lineItemsGrid.getDataProvider().refreshAll();
        clearEditor();
    }

    private void clearEditor() {
        chargeNoField.clear();
        descriptionField.clear();
        lineDateFromField.clear();
        lineDateToField.clear();
        lineDueDateField.clear();
        qtyField.clear();
        priceField.clear();
        qty2Field.clear();
        price2Field.clear();
        qty3Field.clear();
        price3Field.clear();
    }

    // ────────────────────────────────────────────────────────────────────────
    // Tab navigation
    // ────────────────────────────────────────────────────────────────────────

    private void goToTab2() {
        // Select the charge lines tab programmatically
        chargeLinesTab.getElement().callJsFunction("click");
    }

    private void goToTab1() {
        parametersTab.getElement().callJsFunction("click");
    }

    // ────────────────────────────────────────────────────────────────────────
    // Validation
    // ────────────────────────────────────────────────────────────────────────

    private boolean validateParameters() {
        if (isBlank(propertyNo1Field.getValue())) {
            notifications.create("Initial Property No. is required.")
                    .withType(Notifications.Type.WARNING).show();
            propertyNo1Field.focus();
            return false;
        }
        if (invoiceDateField.getValue() == null) {
            notifications.create("Invoice Date is required.")
                    .withType(Notifications.Type.WARNING).show();
            invoiceDateField.focus();
            return false;
        }
        if (generatePeriodFromField.getValue() == null
                || generatePeriodToField.getValue() == null) {
            notifications.create("Generate Period From and To are required.")
                    .withType(Notifications.Type.WARNING).show();
            generatePeriodFromField.focus();
            return false;
        }
        return true;
    }

    // ────────────────────────────────────────────────────────────────────────
    // Build params DTO from Tab 1 fields
    // ────────────────────────────────────────────────────────────────────────

    private GenInvoiceParams buildParams() {
        GenInvoiceParams p = new GenInvoiceParams();
        p.setPropertyNo1(propertyNo1Field.getValue());
        p.setPropertyNo2(isBlank(propertyNo2Field.getValue())
                ? "ZZZZZZZZZZZZ" : propertyNo2Field.getValue());
        p.setInvoiceDate(invoiceDateField.getValue());
        p.setGeneratePeriodFrom(generatePeriodFromField.getValue());
        p.setGeneratePeriodTo(generatePeriodToField.getValue());
        String month = chargesMonthField.getValue();
        p.setChargesMonth(month != null ? Integer.parseInt(month) : null);
        String tax = taxInvTypeField.getValue();
        p.setTaxInvType(taxInvTypeField.isVisible() && tax != null
                ? Integer.parseInt(tax) : 0);
        p.setCheckDuplicate(checkDuplicateField.getValue());
        return p;
    }

    // ────────────────────────────────────────────────────────────────────────
    // Lookup stubs
    // ────────────────────────────────────────────────────────────────────────

    private void openPropertyLookup(TextField target) {
        // TODO: open SelectProperty lookup view, set target value on selection
        notifications.create("Property lookup not yet implemented.")
                .withType(Notifications.Type.WARNING).show();
    }

    private void openChargeLookup() {
        // TODO: open SelectTrxnCodeBill lookup, set chargeNoField + description
        notifications.create("Charge code lookup not yet implemented.")
                .withType(Notifications.Type.WARNING).show();
    }

    // ────────────────────────────────────────────────────────────────────────
    // Utilities
    // ────────────────────────────────────────────────────────────────────────

    private boolean isBlank(String v) { return v == null || v.isBlank(); }

    private String nullSafe(String v) { return v != null ? v : ""; }

    private String decStr(BigDecimal v) {
        return v != null ? v.toPlainString() : "";
    }

    private BigDecimal parseBD(String v) {
        if (v == null || v.isBlank()) return BigDecimal.ZERO;
        try { return new BigDecimal(v.trim()); }
        catch (NumberFormatException e) { return BigDecimal.ZERO; }
    }
}
