package com.company.pmsmain.view.invhdr;

import com.company.pmsmain.entity.Invhdr;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "invhdrs", layout = MainView.class)
@ViewController(id = "Invhdr.list")
@ViewDescriptor(path = "invhdr-list-view.xml")
@LookupComponent("invhdrsDataGrid")
@DialogMode(width = "64em")
public class InvhdrListView extends StandardListView<Invhdr> {
}