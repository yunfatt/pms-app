package com.company.pmsmain.view.bank;

import com.company.pmsmain.entity.Bank;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "banks", layout = MainView.class)
@ViewController(id = "Bank.list")
@ViewDescriptor(path = "bank-list-view.xml")
@LookupComponent("banksDataGrid")
@DialogMode(width = "64em")
public class BankListView extends StandardListView<Bank> {
}