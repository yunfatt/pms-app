package com.company.pmsmain.view.main;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "not-implemented/:name", layout = MainView.class)
@ViewController(id = "NotImplementedView")
@ViewDescriptor(path = "not-implemented-view.xml")
public class NotImplementedView extends StandardView implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Get name from URL and convert slug back to title
        // e.g. "customer-transactions" → "Customer Transactions"
        String slug = event.getRouteParameters()
                .get("name")
                .orElse("page");

        String title = toTitleCase(slug);

        buildUi(title);
    }

    private void buildUi(String title) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Icon
        Div iconWrapper = new Div();
        com.vaadin.flow.component.icon.Icon icon = VaadinIcon.TOOLS.create();
        icon.setSize("64px");
        icon.getStyle().set("color", "var(--lumo-contrast-30pct)");
        iconWrapper.add(icon);
        iconWrapper.getStyle().set("margin-bottom", "16px");

        // Title — from menu item name
        H2 heading = new H2(title);
        heading.getStyle()
                .set("margin", "0")
                .set("color", "var(--lumo-contrast-70pct)");

        // Subtitle
        Span subtitle = new Span("This feature is under development and will be available soon.");
        subtitle.getStyle()
                .set("color", "var(--lumo-contrast-50pct)")
                .set("font-size", "var(--lumo-font-size-m)")
                .set("margin-top", "8px");

        layout.add(iconWrapper, heading, subtitle);
        getContent().removeAll();
        getContent().add(layout);
    }

    // Convert "customer-transactions" → "Customer Transactions"
    private String toTitleCase(String slug) {
        String[] words = slug.replace("-", " ").split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return result.toString().trim();
    }
}