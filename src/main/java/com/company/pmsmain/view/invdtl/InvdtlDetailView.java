package com.company.pmsmain.view.invdtl;

import com.company.pmsmain.entity.Invdtl;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "invdtls/:id", layout = MainView.class)
@ViewController(id = "Invdtl.detail")
@ViewDescriptor(path = "invdtl-detail-view.xml")
@EditedEntityContainer("invdtlDc")
public class InvdtlDetailView extends StandardDetailView<Invdtl> {
}