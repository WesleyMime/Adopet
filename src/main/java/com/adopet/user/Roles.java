package com.adopet.user;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    ABRIGO,
    TUTOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
