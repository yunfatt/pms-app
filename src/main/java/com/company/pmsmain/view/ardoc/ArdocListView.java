package com.company.pmsmain.view.ardoc;

import com.company.pmsmain.entity.Ardoc;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.View.InitEvent;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "ardocs", layout = MainView.class)
@ViewController(id = "Ardoc.list")
@ViewDescriptor(path = "ardoc-list-view.xml")
@LookupComponent("ardocsDataGrid")
@DialogMode(width = "80em")
public class ArdocListView extends StandardListView<Ardoc> {

    @ViewComponent
    private DataGrid<Ardoc> ardocsDataGrid;

    @Subscribe
    public void onInit(final InitEvent event) {

        ardocsDataGrid.addColumn(Ardoc::getTrxncode)
                .setKey("trxncode").setHeader("Trxn Code").setSortable(true);

        ardocsDataGrid.addColumn(Ardoc::getDescription)
                .setKey("description").setHeader("Description").setSortable(true);

        ardocsDataGrid.addColumn(e -> formatBillTo(e.getBillto()))
                .setKey("billto").setHeader("Bill To").setSortable(true);

        ardocsDataGrid.addColumn(e -> formatTtype(e.getTtype()))
                .setKey("ttype").setHeader("Trxn Type").setSortable(true);

        ardocsDataGrid.addColumn(e -> formatPtype(e.getPtype()))
                .setKey("ptype").setHeader("P/B Type").setSortable(true);

        ardocsDataGrid.addColumn(e -> formatShortBool(e.getDeposit()))
                .setKey("deposit").setHeader("Deposit").setSortable(true);

        ardocsDataGrid.addColumn(Ardoc::getRefundcode)
                .setKey("refundcode").setHeader("Refund Code").setSortable(true);

        ardocsDataGrid.addColumn(e -> formatShortBool(e.getChargeinterest()))
                .setKey("chargeinterest").setHeader("Chg Int.").setSortable(true);

        ardocsDataGrid.addColumn(e -> e.getTerm() == null ? "" : String.valueOf(e.getTerm()))
                .setKey("term").setHeader("Term").setSortable(true);

        ardocsDataGrid.addColumn(e -> e.getIntrate() == null ? "" : e.getIntrate().toPlainString())
                .setKey("intrate").setHeader("Int Rate").setSortable(true);

        ardocsDataGrid.addColumn(e -> formatShortBool(e.getAutotransfer()))
                .setKey("autotransfer").setHeader("Auto Transfer").setSortable(true);

        ardocsDataGrid.addColumn(Ardoc::getClassification)
                .setKey("classification").setHeader("Class.").setSortable(true);

        ardocsDataGrid.addColumn(e -> formatShortBool(e.getUtility()))
                .setKey("utility").setHeader("Utility").setSortable(true);

        ardocsDataGrid.addColumn(Ardoc::getReadingfrom)
                .setKey("readingfrom").setHeader("Reading From").setSortable(true);
    }

    private String formatBillTo(Character c) {
        if (c == null) return "";
        return switch (c) {
            case 'O' -> "Owner";
            case 'T' -> "Tenant";
            default -> String.valueOf(c);
        };
    }

    private String formatTtype(Character c) {
        if (c == null) return "";
        return switch (c) {
            case 'B' -> "Billing";
            case 'P' -> "Payment";
            default -> String.valueOf(c);
        };
    }

    private String formatPtype(Character c) {
        if (c == null) return "";
        return switch (c) {
            case 'P' -> "Payment";
            case 'C' -> "Credit Note";
            case 'B' -> "Billing";
            case 'R' -> "Refund";
            default -> String.valueOf(c);
        };
    }

    private String formatShortBool(Short s) {
        if (s == null) return "";
        return s != 0 ? "Yes" : "No";
    }
}