package com.company.pmsmain.multicompany.filter;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.multicompany.context.TenantContext;
import com.company.pmsmain.multicompany.resolver.CompanyResolver;
import com.company.pmsmain.multicompany.service.CompanyRegistryService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CompanyContextFilter extends OncePerRequestFilter {

    public static final String SESSION_COMPANY_KEY = "CURRENT_COMPANY";
    public static final String COOKIE_COMPANY_KEY = "CURRENT_COMPANY";

    private final CompanyResolver companyResolver;
    private final CompanyRegistryService companyRegistryService;

    public CompanyContextFilter(CompanyResolver companyResolver,
                                CompanyRegistryService companyRegistryService) {
        this.companyResolver = companyResolver;
        this.companyRegistryService = companyRegistryService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String uri = request.getRequestURI();
            HttpSession session = request.getSession(false);

            // On unauthenticated /login requests (i.e. after logout),
            // clear company cookie and session so user must re-enter company
            if (uri.startsWith("/login")) {
                boolean isAuthenticated = session != null
                        && session.getAttribute("SPRING_SECURITY_CONTEXT") != null;
                if (!isAuthenticated) {
                    expireCompanyCookie(response, request.isSecure());
                    if (session != null) {
                        session.removeAttribute(SESSION_COMPANY_KEY);
                    }
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            String companyKey = companyResolver.resolveSubdomain(request);
            if (companyKey == null) {
                companyKey = companyResolver.resolveQueryParam(request);
            }

            // 1. If company comes from URL, validate and check for company switch
            if (companyKey != null && !companyKey.isBlank()) {
                String normalised = companyKey.trim().toUpperCase();
                AppCompany company = companyRegistryService.findByCompanyCode(normalised);

                if (company != null) {
                    String existingCompany = session != null
                            ? (String) session.getAttribute(SESSION_COMPANY_KEY)
                            : null;

                    if (existingCompany != null && !existingCompany.equals(normalised)) {
                        // Company switch detected — invalidate old session, set new company, redirect to login
                        session.invalidate();
                        session = request.getSession(true);
                        session.setAttribute(SESSION_COMPANY_KEY, normalised);
                        addCompanyCookie(response, normalised, request.isSecure());
                        TenantContext.setCompanyCode(normalised);
                        response.sendRedirect("/login");
                        return;
                    }
                    if (session == null) session = request.getSession(true);
                    session.setAttribute(SESSION_COMPANY_KEY, normalised);
                    addCompanyCookie(response, normalised, request.isSecure());
                    companyKey = normalised;
                } else {
                    companyKey = null; // invalid — treat as not provided
                }
            }

            // 2. Restore from session
            if ((companyKey == null || companyKey.isBlank()) && session != null) {
                Object saved = session.getAttribute(SESSION_COMPANY_KEY);
                if (saved instanceof String s && !s.isBlank()) {
                    companyKey = s;
                }
            }

            // 3. Restore from cookie — validate before trusting
            if (companyKey == null || companyKey.isBlank()) {
                String rawCookie = readCompanyCookie(request);
                if (rawCookie != null && !rawCookie.isBlank()) {
                    String normalisedCookie = rawCookie.trim().toUpperCase();
                    AppCompany company = companyRegistryService.findByCompanyCode(normalisedCookie);
                    if (company != null) {
                        companyKey = normalisedCookie;
                        if (session == null) session = request.getSession(true);
                        session.setAttribute(SESSION_COMPANY_KEY, companyKey);
                        addCompanyCookie(response, companyKey, request.isSecure());
                    } else {
                        expireCompanyCookie(response, request.isSecure());
                    }
                }
            }

            // 4. Set TenantContext if resolved
            if (companyKey != null && !companyKey.isBlank()) {
                TenantContext.setCompanyCode(companyKey);
            }

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri == null) return false;

        if (isStaticPath(uri)) return true;

        if (uri.startsWith("/enter-company")) return true;

        String forwardUri = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
        if (forwardUri != null && forwardUri.startsWith("/enter-company")) return true;

        return false;
    }

    private boolean isStaticPath(String uri) {
        return uri.startsWith("/VAADIN/")
                || uri.startsWith("/themes/")
                || uri.startsWith("/images/")
                || uri.startsWith("/icons/")
                || uri.startsWith("/manifest.webmanifest")
                || uri.startsWith("/sw.js")
                || uri.startsWith("/offline.html")
                || uri.startsWith("/offline-stub.html")
                || uri.startsWith("/favicon.ico");
    }

    private void addCompanyCookie(HttpServletResponse response, String companyKey, boolean secure) {
        Cookie cookie = new Cookie(COOKIE_COMPANY_KEY, companyKey);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7); // 7 days
        response.addCookie(cookie);
    }

    private void expireCompanyCookie(HttpServletResponse response, boolean secure) {
        Cookie cookie = new Cookie(COOKIE_COMPANY_KEY, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String readCompanyCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (COOKIE_COMPANY_KEY.equals(cookie.getName())) {
                String value = cookie.getValue();
                if (value == null || value.isBlank()) return null;
                return value;
            }
        }
        return null;
    }
}
