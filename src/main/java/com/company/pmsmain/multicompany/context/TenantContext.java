package com.company.pmsmain.multicompany.context;

public final class TenantContext {

    private static final ThreadLocal<String> CURRENT_COMPANY = new ThreadLocal<>();

    private TenantContext() {
        // prevent instantiation
    }

    public static void setCompanyCode(String companyCode) {
        CURRENT_COMPANY.set(companyCode);
    }

    public static String getCompanyCode() {
        return CURRENT_COMPANY.get();
    }

    public static void clear() {
        CURRENT_COMPANY.remove();
    }
}