package com.poluhin.ss.demo.domain.security;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@ToString
@AllArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String authority;


    @Override
    public String getAuthority() {
        return authority;
    }
}
