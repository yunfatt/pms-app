package com.company.pmsmain.view.property;

import com.company.pmsmain.entity.Property;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "properties", layout = MainView.class)
@ViewController(id = "Property.list")
@ViewDescriptor(path = "property-list-view.xml")
@LookupComponent("propertiesDataGrid")
@DialogMode(width = "64em")
public class PropertyListView extends StandardListView<Property> {
}