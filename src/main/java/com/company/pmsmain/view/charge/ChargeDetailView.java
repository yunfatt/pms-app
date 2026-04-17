package com.company.pmsmain.view.charge;

import com.company.pmsmain.entity.Charge;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "charges/:id", layout = MainView.class)
@ViewController(id = "Charge.detail")
@ViewDescriptor(path = "charge-detail-view.xml")
@EditedEntityContainer("chargeDc")
public class ChargeDetailView extends StandardDetailView<Charge> {
}