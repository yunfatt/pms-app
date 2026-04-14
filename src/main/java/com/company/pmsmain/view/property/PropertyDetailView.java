package com.company.pmsmain.view.property;

import com.company.pmsmain.entity.Property;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "properties/:id", layout = MainView.class)
@ViewController(id = "Property.detail")
@ViewDescriptor(path = "property-detail-view.xml")
@EditedEntityContainer("propertyDc")
public class PropertyDetailView extends StandardDetailView<Property> {
}