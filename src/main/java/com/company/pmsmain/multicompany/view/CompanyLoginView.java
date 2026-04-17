package com.company.pmsmain.multicompany.view;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.multicompany.filter.CompanyContextFilter;
import com.company.pmsmain.multicompany.service.CompanyRegistryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.http.HttpSession;

@Route("enter-company")
@PageTitle("Enter Company")
@AnonymousAllowed
public class CompanyLoginView extends VerticalLayout {

    private final CompanyRegistryService companyRegistryService;

    public CompanyLoginView(CompanyRegistryService companyRegistryService) {
        this.companyRegistryService = companyRegistryService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2("Enter Your Company Code");
        Paragraph hint = new Paragraph("e.g. DEMO");

        // Show error if redirected with ?error=invalid
        String error = VaadinServletRequest.getCurrent() != null
                ? VaadinServletRequest.getCurrent().getParameter("error")
                : null;
        if ("invalid".equals(error)) {
            Notification notification = Notification.show(
                    "Company not found or inactive. Please enter a valid company code.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }


        TextField companyCodeField = new TextField("Company Code");
        companyCodeField.setWidth("300px");
        companyCodeField.setPlaceholder("Enter company code");

        Button continueBtn = new Button("Continue", e -> {
            String input = companyCodeField.getValue();
            if (input == null || input.isBlank()) {
                Notification.show("Please enter a company code.");
                return;
            }

            AppCompany company = companyRegistryService.findByCompanyCode(input.trim());
            if (company == null) {
                Notification.show("Company not found or inactive. Please try again.");
                return;
            }

            // Normalise and store in session — same format as CompanyContextFilter
            String normalised = company.getCompanyCode()
                    .trim().toUpperCase();

            VaadinServletRequest vaadinRequest = VaadinServletRequest.getCurrent();
            if (vaadinRequest != null) {
                HttpSession session = vaadinRequest.getHttpServletRequest()
                        .getSession(true);
                session.setAttribute(CompanyContextFilter.SESSION_COMPANY_KEY, normalised);
            }

            // Navigate to login
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        add(title, hint, companyCodeField, continueBtn);
    }
}