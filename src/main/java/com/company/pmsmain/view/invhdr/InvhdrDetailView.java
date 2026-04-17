package com.company.pmsmain.view.invhdr;

import com.company.pmsmain.entity.Invdtl;
import com.company.pmsmain.entity.Invhdr;
import com.company.pmsmain.entity.Property;
import com.company.pmsmain.entity.key.InvdtlCompKey;
import com.company.pmsmain.util.DocumentFormatUtils;
import com.company.pmsmain.view.invdtl.InvdtlDetailView;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

@Route(value = "invhdrs/:id", layout = MainView.class)
@ViewController(id = "Invhdr.detail")
@ViewDescriptor(path = "invhdr-detail-view.xml")
@EditedEntityContainer("invhdrDc")
public class InvhdrDetailView extends StandardDetailView<Invhdr> {

    @ViewComponent
    private CollectionContainer<Invdtl> invdtlsDc;

    @ViewComponent
    private CollectionLoader<Invdtl> invdtlsDl;

    @ViewComponent
    private DataGrid<Invdtl> invdtlsDataGrid;

    @ViewComponent
    private TypedTextField<String> invnoField;

    @ViewComponent
    private TypedTextField<String> propertynoField;

    @ViewComponent
    private TypedTextField<String> custnameField;

    @ViewComponent
    private JmixSelect<Character> custtypeField;

    @ViewComponent
    private Button addItemBtn;

    @ViewComponent
    private Button removeItemBtn;

    @ViewComponent
    private Button moveUpBtn;

    @ViewComponent
    private Button moveDownBtn;

    @ViewComponent
    private Button saveAndCloseButton;

    @ViewComponent
    private Button lookupPropertyBtn;

    @ViewComponent
    private DataContext dataContext;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private DialogWindows dialogWindows;

    private boolean recalculating = false;

    // ── Init entity (new records only) ────────────────────────────────────────

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Invhdr> event) {
        Invhdr invhdr = event.getEntity();
        if (invhdr.getTrxndate() == null) {
            invhdr.setTrxndate(new Date());
        }
        if (invhdr.getCusttype() == null) {
            invhdr.setCusttype('O');
        }
    }

    // ── Init ──────────────────────────────────────────────────────────────────

    @Subscribe
    public void onInit(final InitEvent event) {
        setupButtons();
        setupCustType();
        setupPropertyLookup();
        setupLineDoubleClick();
    }

    private void setupButtons() {
        addItemBtn.addClickListener(click -> addLine());
        removeItemBtn.addClickListener(click -> removeSelectedLine());
        moveUpBtn.addClickListener(click -> moveLine(-1));
        moveDownBtn.addClickListener(click -> moveLine(1));
        saveAndCloseButton.addClickListener(click -> saveAndClose());
    }

    private void setupCustType() {
        custtypeField.setItems(Arrays.asList('O', 'T'));
        custtypeField.setItemLabelGenerator(c -> {
            if (c == null) return "";
            return switch (c) {
                case 'O' -> "Owner";
                case 'T' -> "Tenant";
                default  -> String.valueOf(c);
            };
        });
    }

    private void setupPropertyLookup() {
        lookupPropertyBtn.addClickListener(click ->
                dialogWindows.lookup(this, Property.class)
                        .withSelectHandler(properties -> {
                            if (!properties.isEmpty()) {
                                Property p = properties.iterator().next();
                                getEditedEntity().setPropertyno(p.getPropertyno());
                                propertynoField.setValue(p.getPropertyno());
                                refreshCustomerDisplay(p.getPropertyno());
                            }
                        })
                        .open()
        );

        propertynoField.addValueChangeListener(e -> {
            getEditedEntity().setPropertyno(e.getValue());
            refreshCustomerDisplay(e.getValue());
        });
    }

    private void setupLineDoubleClick() {
        invdtlsDataGrid.addItemDoubleClickListener(e -> {
            if (e.getItem() != null) {
                openLineDialog(e.getItem());
            }
        });
    }

    // ── Ready ─────────────────────────────────────────────────────────────────

    @Subscribe
    public void onReady(final ReadyEvent event) {
        refreshInvnoDisplay();

        Invhdr invhdr = getEditedEntity();
        if (invhdr.getInvno() != null) {
            invdtlsDl.setParameter("invno", invhdr.getInvno());
            invdtlsDl.load();
        }

        refreshCustomerDisplay(invhdr.getPropertyno());
    }

    // ── Pre-save ──────────────────────────────────────────────────────────────

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPreSave(final DataContext.PreSaveEvent event) {
        Invhdr invhdr = getEditedEntity();
        if (invhdr.getTrxndate() == null) {
            invhdr.setTrxndate(new Date());
        }
        recalculateTotals();
        refreshInvnoDisplay();
    }

    // ── Item property change ──────────────────────────────────────────────────

    @Subscribe(id = "invdtlsDc", target = Target.DATA_CONTAINER)
    public void onInvdtlsDcItemPropertyChange(
            CollectionContainer.ItemPropertyChangeEvent<Invdtl> event) {
        if (recalculating) return;

        String property = event.getProperty();
        Invdtl item = event.getItem();

        if ("qty".equals(property) || "price".equals(property)) {
            recalculateLineAmount(item);
            recalculateTotals();
        }

        if ("netamt".equals(property) || "gstamt".equals(property)
                || "amount".equals(property)) {
            recalculateTotals();
        }
    }

    // ── Line item dialog ──────────────────────────────────────────────────────

    private void openLineDialog(Invdtl item) {
        dialogWindows.detail(this, Invdtl.class)
                .editEntity(item)
                .withViewClass(InvdtlDetailView.class)
                .withAfterCloseListener(e -> {
                    recalculateTotals();
                    invdtlsDataGrid.getDataProvider().refreshAll();
                })
                .open();
    }

    // ── Line operations ───────────────────────────────────────────────────────

    private void addLine() {
        Invdtl item = dataContext.create(Invdtl.class);

        InvdtlCompKey key = new InvdtlCompKey();
        key.setInvno(getEditedEntity().getInvno());
        key.setLinenum(getNextLineNum());
        item.setId(key);

        item.setQty(BigDecimal.ZERO);
        item.setPrice(BigDecimal.ZERO);
        item.setNetamt(BigDecimal.ZERO);
        item.setGstamt(BigDecimal.ZERO);
        item.setAmount(BigDecimal.ZERO);

        invdtlsDc.getMutableItems().add(item);
        recalculateTotals();

        // Open new line in dialog immediately
        openLineDialog(item);
    }

    private void removeSelectedLine() {
        Invdtl selected = invdtlsDataGrid.getSingleSelectedItem();
        if (selected != null) {
            invdtlsDc.getMutableItems().remove(selected);
            recalculateTotals();
        }
    }

    private void moveLine(int direction) {
        Invdtl selected = invdtlsDataGrid.getSingleSelectedItem();
        if (selected == null) return;

        List<Invdtl> items = invdtlsDc.getMutableItems();
        int currentIndex = items.indexOf(selected);
        int targetIndex  = currentIndex + direction;

        if (targetIndex < 0 || targetIndex >= items.size()) return;

        Invdtl other = items.get(targetIndex);
        Short tempLineNum = selected.getId().getLinenum();
        selected.getId().setLinenum(other.getId().getLinenum());
        other.getId().setLinenum(tempLineNum);

        items.set(currentIndex, other);
        items.set(targetIndex, selected);

        invdtlsDataGrid.getDataProvider().refreshAll();
        invdtlsDataGrid.select(selected);
    }

    private void saveAndClose() {
        if (invdtlsDc.getItems().isEmpty()) {
            Notification.show("Invoice must have at least one item",
                    3000, Notification.Position.MIDDLE);
            return;
        }

        boolean invalidQty = invdtlsDc.getItems().stream()
                .anyMatch(item -> item.getQty() == null
                        || item.getQty().compareTo(BigDecimal.ZERO) <= 0);

        if (invalidQty) {
            Notification.show("All items must have quantity > 0",
                    3000, Notification.Position.MIDDLE);
            return;
        }

        closeWithSave();
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    private void recalculateLineAmount(Invdtl item) {
        recalculating = true;
        try {
            BigDecimal qty    = nz(item.getQty());
            BigDecimal price  = nz(item.getPrice());
            BigDecimal netamt = qty.multiply(price);
            item.setNetamt(netamt);
            item.setAmount(netamt.add(nz(item.getGstamt())));
            invdtlsDataGrid.getDataProvider().refreshItem(item);
        } finally {
            recalculating = false;
        }
    }

    private void recalculateTotals() {
        BigDecimal netamt = invdtlsDc.getItems().stream()
                .map(Invdtl::getNetamt).map(this::nz)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalgst = invdtlsDc.getItems().stream()
                .map(Invdtl::getGstamt).map(this::nz)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Invhdr invhdr = getEditedEntity();
        invhdr.setNetamt(netamt);
        invhdr.setTotalgst(totalgst);
        invhdr.setAmount(netamt.add(totalgst));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void refreshCustomerDisplay(String propertyno) {
        if (propertyno == null || propertyno.isBlank()) {
            custnameField.setValue("");
            return;
        }

        Optional<Property> property = dataManager.load(Property.class)
                .id(propertyno)
                .optional();

        property.ifPresentOrElse(p -> {
            String display = List.of(
                            p.getUnitno() != null ? p.getUnitno() : "",
                            p.getAddr1()  != null ? p.getAddr1()  : ""
                    ).stream()
                    .filter(s -> !s.isBlank())
                    .reduce((a, b) -> a + " - " + b)
                    .orElse("");
            custnameField.setValue(display);
        }, () -> custnameField.setValue("Not found"));
    }

    private void refreshInvnoDisplay() {
        invnoField.setValue(
                DocumentFormatUtils.formatDocNo(getEditedEntity().getInvno()));
    }

    private Short getNextLineNum() {
        return invdtlsDc.getItems().stream()
                .map(i -> i.getId().getLinenum())
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .map(n -> (short) (n + 1))
                .orElse((short) 1);
    }

    private BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}