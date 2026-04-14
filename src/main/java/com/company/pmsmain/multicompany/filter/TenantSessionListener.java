package com.company.pmsmain.multicompany.filter;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class TenantSessionListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionDestroyListener(e -> {
            try {
                var wrappedSession = e.getSession().getSession();
                if (wrappedSession != null) {
                    wrappedSession.removeAttribute(CompanyContextFilter.SESSION_COMPANY_KEY);
                }
            } catch (IllegalStateException ignored) {
                // session already invalidated
            }
        });
    }
}
