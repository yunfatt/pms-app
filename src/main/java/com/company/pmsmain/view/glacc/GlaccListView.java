package com.company.pmsmain.view.glacc;

import com.company.pmsmain.entity.Glacc;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "glaccounts", layout = MainView.class)
@ViewController(id = "Glacc.list")
@ViewDescriptor(path = "glacc-list-view.xml")
@LookupComponent("glaccDataGrid")
@DialogMode(width = "64em")
public class GlaccListView extends StandardListView<Glacc> {
}
