package com.company.pmsmain.view;

import com.company.pmsmain.view.main.NavigationTitleService;
import com.vaadin.flow.component.html.H3;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseListView<E> extends StandardListView<E> {

    @Autowired
    private NavigationTitleService navigationTitleService;

    @Subscribe
    public void onInit(InitEvent event) {
        getContent().getStyle()
                .set("background-color", "#f7fbf8");
        addPageTitle();
    }

    private void addPageTitle() {
        String title = navigationTitleService.getCurrentTitle();
        if (title == null || title.isBlank()) return;

        H3 heading = new H3(title);
        heading.getStyle()
                .set("margin", "0 0 4px 0")
                .set("padding", "8px 16px 0 16px")
                .set("font-size", "15px")
                .set("font-weight", "500")
                .set("color", "#1b3a6b");

        getContent().addComponentAsFirst(heading);
    }
}