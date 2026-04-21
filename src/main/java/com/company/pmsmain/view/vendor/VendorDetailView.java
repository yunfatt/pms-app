package com.company.pmsmain.view.vendor;

import com.company.pmsmain.entity.Vendor;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "suppliers/:id", layout = MainView.class)
@ViewController(id = "Vendor.detail")
@ViewDescriptor(path = "vendor-detail-view.xml")
@EditedEntityContainer("vendorDc")
public class VendorDetailView extends StandardDetailView<Vendor> {
}