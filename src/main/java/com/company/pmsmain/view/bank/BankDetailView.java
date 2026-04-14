package com.company.pmsmain.view.bank;

import com.company.pmsmain.entity.Bank;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "banks/:id", layout = MainView.class)
@ViewController(id = "Bank.detail")
@ViewDescriptor(path = "bank-detail-view.xml")
@EditedEntityContainer("bankDc")
public class BankDetailView extends StandardDetailView<Bank> {
}