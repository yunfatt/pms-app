package com.company.pmsmain.view.customer;

import com.company.pmsmain.entity.Customer;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "customers", layout = MainView.class)
@ViewController(id = "Customer.list")
@ViewDescriptor(path = "customer-list-view.xml")
@LookupComponent("customersDataGrid")
@DialogMode(width = "64em")
public class CustomerListView extends StandardListView<Customer> {
}