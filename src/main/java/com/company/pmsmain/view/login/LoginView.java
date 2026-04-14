package com.company.pmsmain.view.login;

import com.company.pmsmain.entity.AppCompany;
import com.company.pmsmain.multicompany.filter.CompanyContextFilter;
import com.company.pmsmain.multicompany.service.CompanyRegistryService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import io.jmix.core.CoreProperties;
import io.jmix.core.MessageTools;
import io.jmix.core.security.AccessDeniedException;
import io.jmix.flowui.component.loginform.JmixLoginForm;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.kit.component.loginform.JmixLoginI18n;
import io.jmix.flowui.view.*;
import io.jmix.securityflowui.authentication.AuthDetails;
import io.jmix.securityflowui.authentication.LoginViewSupport;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@Route(value = "login")
@ViewController(id = "LoginView")
@ViewDescriptor(path = "login-view.xml")
public class LoginView extends StandardView implements LocaleChangeObserver {

    private static final Logger log = LoggerFactory.getLogger(LoginView.class);

    @Autowired
    private CoreProperties coreProperties;

    @Autowired
    private LoginViewSupport loginViewSupport;

    @Autowired
    private MessageTools messageTools;

    @ViewComponent
    private JmixLoginForm login;

    @ViewComponent
    private MessageBundle messageBundle;

    @Autowired
    private CompanyRegistryService companyRegistryService;

    @Value("${ui.login.defaultUsername:}")
    private String defaultUsername;

    @Value("${ui.login.defaultPassword:}")
    private String defaultPassword;

    @Subscribe
    public void onInit(final InitEvent event) {
        initLocales();
        initDefaultCredentials();
        redirectIfNoCompany();
    }
    private void redirectIfNoCompany() {
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if (request == null) return;

        // 1. Check ?company= param directly in current request
        String companyParam = request.getParameter("company");

        // 2. If not found, extract from Spring Security's saved request URL
        if (companyParam == null || companyParam.isBlank()) {
            HttpSession session = request.getHttpServletRequest().getSession(false);
            if (session != null) {
                Object savedRequest = session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                if (savedRequest != null) {
                    String savedUrl = savedRequest.toString();
                    // e.g. "DefaultSavedRequest [http://localhost:8100/?company=Company_A&continue]"
                    int idx = savedUrl.indexOf("company=");
                    if (idx != -1) {
                        int end = savedUrl.indexOf("&", idx);
                        if (end == -1) end = savedUrl.indexOf("]", idx);
                        if (end == -1) end = savedUrl.length();
                        companyParam = savedUrl.substring(idx + "company=".length(), end);
                    }
                }
            }
        }

        // 3. If company found from URL or saved request, validate and store in session
        if (companyParam != null && !companyParam.isBlank()) {
            String normalised = companyParam.trim().toUpperCase();

            // Validate company exists and is active
            AppCompany company = companyRegistryService.findByCompanyCode(normalised);
            if (company == null) {
                // Invalid company — redirect to entry page with error
                UI.getCurrent().getPage().setLocation("/enter-company?error=invalid");
                return;
            }

            // Valid — store in session and stay on login
            request.getHttpServletRequest().getSession(true)
                    .setAttribute(CompanyContextFilter.SESSION_COMPANY_KEY, normalised);
            return;
        }

        // 4. Check session
        HttpSession session = request.getHttpServletRequest().getSession(false);
        if (session != null) {
            Object company = session.getAttribute(CompanyContextFilter.SESSION_COMPANY_KEY);
            if (company instanceof String s && !s.isBlank()) {
                return;
            }
        }

        // 5. Check cookie
        Cookie[] cookies = request.getHttpServletRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CompanyContextFilter.COOKIE_COMPANY_KEY.equals(cookie.getName())
                        && cookie.getValue() != null
                        && !cookie.getValue().isBlank()) {
                    String normalised = cookie.getValue().trim();

                    // Validate cookie company still exists and is active
                    AppCompany company = companyRegistryService.findByCompanyCode(normalised);
                    if (company == null) {
                        // Stale cookie — redirect to entry page
                        UI.getCurrent().getPage().setLocation("/enter-company?error=invalid");
                        return;
                    }

                    request.getHttpServletRequest().getSession(true)
                            .setAttribute(CompanyContextFilter.SESSION_COMPANY_KEY, normalised);
                    return;
                }
            }
        }

        // Nothing found — redirect to entry page
        UI.getCurrent().getPage().setLocation("/enter-company");
    }
    private void initLocales() {
        LinkedHashMap<Locale, String> locales = coreProperties.getAvailableLocales().stream()
                .collect(Collectors.toMap(Function.identity(), messageTools::getLocaleDisplayName, (s1, s2) -> s1,
                        LinkedHashMap::new));

        ComponentUtils.setItemsMap(login, locales);

        login.setSelectedLocale(VaadinSession.getCurrent().getLocale());
    }

    private void initDefaultCredentials() {
        if (StringUtils.isNotBlank(defaultUsername)) {
            login.setUsername(defaultUsername);
        }

        if (StringUtils.isNotBlank(defaultPassword)) {
            login.setPassword(defaultPassword);
        }
    }

    @Subscribe("login")
    public void onLogin(final LoginEvent event) {
        try {
            loginViewSupport.authenticate(
                    AuthDetails.of(event.getUsername(), event.getPassword())
                            .withLocale(login.getSelectedLocale())
                            .withRememberMe(login.isRememberMe())
            );
        } catch (final BadCredentialsException | DisabledException | LockedException | AccessDeniedException e) {
            log.warn("Login failed for user '{}': {}", event.getUsername(), e.toString());
            event.getSource().setError(true);
        }
    }

    @Override
    public void localeChange(final LocaleChangeEvent event) {
        UI.getCurrent().getPage().setTitle(messageBundle.getMessage("LoginView.title"));

        final JmixLoginI18n loginI18n = JmixLoginI18n.createDefault();

        final JmixLoginI18n.JmixForm form = new JmixLoginI18n.JmixForm();
        form.setTitle(messageBundle.getMessage("loginForm.headerTitle"));
        form.setUsername(messageBundle.getMessage("loginForm.username"));
        form.setPassword(messageBundle.getMessage("loginForm.password"));
        form.setSubmit(messageBundle.getMessage("loginForm.submit"));
        form.setForgotPassword(messageBundle.getMessage("loginForm.forgotPassword"));
        form.setRememberMe(messageBundle.getMessage("loginForm.rememberMe"));
        loginI18n.setForm(form);

        final LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle(messageBundle.getMessage("loginForm.errorTitle"));
        errorMessage.setMessage(messageBundle.getMessage("loginForm.badCredentials"));
        errorMessage.setUsername(messageBundle.getMessage("loginForm.errorUsername"));
        errorMessage.setPassword(messageBundle.getMessage("loginForm.errorPassword"));
        loginI18n.setErrorMessage(errorMessage);

        login.setI18n(loginI18n);
    }
}