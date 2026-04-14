package com.company.pmsmain.multicompany.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CompanyResolver {

    public String resolveSubdomain(HttpServletRequest request) {
        String host = request.getServerName();
        if (host == null || host.isBlank()) {
            return null;
        }

        host = host.toLowerCase();

        if ("localhost".equals(host) || "127.0.0.1".equals(host)) {
            return null;
        }

        String[] parts = host.split("\\.");
        if (parts.length < 2) {
            return null;
        }

        return parts[0];
    }

    public String resolveQueryParam(HttpServletRequest request) {
        String company = request.getParameter("company");
        if (company == null || company.isBlank()) {
            return null;
        }
        return company.toLowerCase();
    }
}