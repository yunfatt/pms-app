package com.company.pmsmain.multicompany.service;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.multicompany.context.TenantContext;
import com.company.pmsmain.multicompany.filter.CompanyContextFilter;
import com.vaadin.flow.server.VaadinServletRequest;
import io.jmix.core.UnconstrainedDataManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantDebugService {

    @Autowired
    private UnconstrainedDataManager dataManager;

    public String getCurrentCompanyCode() {
        AppCompany company = getCurrentCompany();
        return company != null ? company.getCompanyCode() : "(invalid company: " + resolveKey() + ")";
    }

    public String getCurrentCompanyName() {
        AppCompany company = getCurrentCompany();
        return company != null ? company.getCompanyName() : "(invalid company)";
    }

    private AppCompany getCurrentCompany() {
        String key = resolveKey();
        System.out.println(">>> TenantDebug resolveKey: [" + key + "]");

        if (key == null || key.isBlank()) return null;

        String normalised = key.trim().toUpperCase();

        AppCompany result = dataManager.load(AppCompany.class)
                .query("e.companyCode = ?1 and e.active = true", normalised)
                .optional()
                .orElse(null);

        System.out.println(">>> TenantDebug result: " + (result != null ? result.getCompanyCode() : "null"));
        return result;
    }

    /**
     * Resolves company key from multiple sources in priority order:
     * 1. TenantContext (set by filter during HTTP request threads)
     * 2. HTTP session (set by CompanyContextFilter / CompanyLoginView)
     */
    private String resolveKey() {
        // 1. TenantContext — set by filter on every HTTP request
        String key = TenantContext.getCompanyCode();
        System.out.println(">>> resolveKey TenantContext: [" + key + "]");
        if (key != null && !key.isBlank()) {
            return key;
        }

        // 2. HTTP session via VaadinServletRequest
        try {
            VaadinServletRequest request = VaadinServletRequest.getCurrent();
            if (request != null) {
                HttpSession session = request.getHttpServletRequest().getSession(false);
                if (session != null) {
                    Object saved = session.getAttribute(CompanyContextFilter.SESSION_COMPANY_KEY);
                    System.out.println(">>> resolveKey session: [" + saved + "]");
                    if (saved instanceof String s && !s.isBlank()) {
                        return s;
                    }
                }
            }
        } catch (Exception ignored) {}

        return null;
    }
}