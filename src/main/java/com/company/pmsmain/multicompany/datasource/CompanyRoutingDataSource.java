package com.company.pmsmain.multicompany.datasource;

import com.company.pmsmain.multicompany.context.TenantContext;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import java.sql.Connection;
import java.sql.SQLException;
public class CompanyRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String key = TenantContext.getCompanyCode();

        if (key == null || key.isBlank()) {
            try {
                VaadinRequest current = VaadinRequest.getCurrent();
                if (current instanceof VaadinServletRequest vsr) {
                    HttpSession session = vsr.getHttpServletRequest().getSession(false);
                    if (session != null) {
                        Object saved = session.getAttribute("CURRENT_COMPANY");
                        if (saved instanceof String s && !s.isBlank()) {
                            key = s;
                        }
                    }
                }
            } catch (Exception ignored) {}
        }

        if (key != null) {
            key = key.trim().toUpperCase();
        }

        System.out.println(">>> Routing key = " + key
                + " | thread = " + Thread.currentThread().getName());
        return key;
    }
    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        System.out.println(">>> Actual DB = " + conn.getCatalog()
                + " | thread = " + Thread.currentThread().getName());
        return conn;
    }
}
