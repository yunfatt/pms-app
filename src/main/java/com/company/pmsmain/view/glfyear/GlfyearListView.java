package com.company.pmsmain.view.glfyear;

import com.company.pmsmain.entity.Glfyear;
import com.company.pmsmain.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.genericfilter.GenericFilter;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import com.vaadin.flow.component.icon.VaadinIcon;

@Route(value = "glfyears", layout = MainView.class)
@ViewController(id = "Glfyear.list")
@ViewDescriptor(path = "glfyear-list-view.xml")
@LookupComponent("glfyearDataGrid")
@DialogMode(width = "64em")
public class GlfyearListView extends StandardListView<Glfyear> {

    @ViewComponent
    private TypedTextField<String> searchField;

    @ViewComponent
    private JmixButton searchButton;

    @ViewComponent
    private JmixButton advancedSearchToggle;

    @ViewComponent
    private GenericFilter genericFilter;

    @ViewComponent
    private CollectionLoader<Glfyear> glfyearDl;

    private boolean advancedMode = false;

    @Subscribe
    public void onInit(InitEvent event) {
        searchField.addKeyPressListener(Key.ENTER, e -> executeSearch());
        searchField.addValueChangeListener(e -> {
            if (e.getValue() == null || e.getValue().isBlank()) {
                executeSearch();
            }
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        glfyearDl.setParameter("searchText", "");
        glfyearDl.load();
    }

    @Subscribe("searchButton")
    public void onSearchButtonClick(ClickEvent<JmixButton> event) {
        executeSearch();
    }

    @Subscribe("advancedSearchToggle")
    public void onAdvancedSearchToggle(ClickEvent<JmixButton> event) {
        advancedMode = !advancedMode;

        if (advancedMode) {
            advancedSearchToggle.setText("Simple");
            advancedSearchToggle.setIcon(VaadinIcon.SEARCH.create());
        } else {
            advancedSearchToggle.setText("Advanced");
            advancedSearchToggle.setIcon(VaadinIcon.FILTER.create());
        }

        searchField.setVisible(!advancedMode);
        searchButton.setVisible(!advancedMode);
        genericFilter.setVisible(advancedMode);

        if (!advancedMode) {
            glfyearDl.setParameter("searchText", "");
            glfyearDl.load();
        } else {
            glfyearDl.removeParameter("searchText");
        }
    }

    private void executeSearch() {
        String text = searchField.getValue();
        glfyearDl.setParameter("searchText", text == null ? "" : text.trim());
        glfyearDl.load();
    }
}
