package com.company.pmsmain.helpdesk.security;

import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(
    name = "HelpDesk User",
    code = "helpdesk-user",
    scope = "UI"
)
public interface HelpdeskRole {

    @ViewPolicy(viewIds = {
        "helpdesk_HelpdeskChatView",
        "helpdesk_HelpdeskTicketsView"
    })
    void helpdeskViews();
}
