package com.company.pmsmain.view.phase;

import com.company.pmsmain.entity.Phase;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Detail view for Phase.
 *
 * Mirrors the Clarion UpdatePhase form's bank-specific group visibility:
 *   HSBC / Citibank / Alliance  → groupHsbc
 *   Public Bank (PBB)           → groupPublicBank
 *   CIMB                        → groupCimb
 *   Ambank (AMB)                → groupAmbank
 *   UOB                         → groupUob
 *
 * When the bank selection changes, only the relevant group is expanded;
 * all others are collapsed — matching the Clarion mutually-exclusive GROUP behaviour.
 */
@Route(value = "phases/:id", layout = MainView.class)
@ViewController(id = "Phase.detail")
@ViewDescriptor(path = "phase-detail-view.xml")
@EditedEntityContainer("phaseDc")
public class PhaseDetailView extends StandardDetailView<Phase> {

    @ViewComponent
    private JmixSelect<String> bankField;

    @ViewComponent
    private Details groupHsbc;
    @ViewComponent
    private Details groupPublicBank;
    @ViewComponent
    private Details groupCimb;
    @ViewComponent
    private Details groupAmbank;
    @ViewComponent
    private Details groupUob;
    @ViewComponent
    private Details groupOtherIds;

    @Subscribe
    public void onReady(ReadyEvent event) {
        applyBankGroupVisibility(getEditedEntity().getBank());

        bankField.addValueChangeListener(e -> applyBankGroupVisibility(e.getValue()));
    }

    /**
     * Show/collapse bank-specific detail sections based on the selected bank code.
     * Mirrors Clarion's mutually-exclusive GROUP show/hide logic.
     */
    private void applyBankGroupVisibility(String bank) {
        boolean isHsbc    = "HSBC".equals(bank) || "CITI".equals(bank) || "ABB".equals(bank);
        boolean isPbb     = "PBB".equals(bank);
        boolean isCimb    = "CIMB".equals(bank);
        boolean isAmb     = "AMB".equals(bank);
        boolean isUob     = "UOB".equals(bank);

        groupHsbc.setOpened(isHsbc);
        groupPublicBank.setOpened(isPbb);
        groupCimb.setOpened(isCimb);
        groupAmbank.setOpened(isAmb);
        groupUob.setOpened(isUob);

        // "Other IDs" section is always available but collapsed by default
        groupOtherIds.setOpened(false);
    }
}
