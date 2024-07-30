package com.ilyass.admin.enumeration;

import java.util.HashSet;
import java.util.Set;

import static com.ilyass.admin.constant.Authority.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_HR(HR_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_USER_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public Set<String> getAuthorities() {
        return new HashSet<>(Set.of(authorities));
    }
}
