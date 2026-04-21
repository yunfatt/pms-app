package com.company.pmsmain.view.glacc;

import com.company.pmsmain.entity.Glacc;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "glaccounts/:id", layout = MainView.class)
@ViewController(id = "Glacc.detail")
@ViewDescriptor(path = "glacc-detail-view.xml")
@EditedEntityContainer("glaccDc")
public class GlaccDetailView extends StandardDetailView<Glacc> {
}
