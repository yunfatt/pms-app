package com.company.pmsmain.view.main;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class NavigationTitleService {

    private String currentTitle = "";

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(String title) {
        this.currentTitle = title;
    }
}