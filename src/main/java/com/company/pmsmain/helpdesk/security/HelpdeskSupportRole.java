package com.company.pmsmain.helpdesk.security;

import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(
    name = "HelpDesk Support",
    code = "helpdesk-support",
    scope = "UI"
)
public interface HelpdeskSupportRole {

    @ViewPolicy(viewIds = {
        "helpdesk_HelpdeskChatView",
        "helpdesk_HelpdeskTicketsView"
    })
    void helpdeskViews();
}
