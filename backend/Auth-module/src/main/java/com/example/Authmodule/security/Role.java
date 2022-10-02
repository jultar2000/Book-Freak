package com.example.Authmodule.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.Authmodule.security.Permission.*;

@Getter
@AllArgsConstructor
public enum Role {

    USER(Sets.newHashSet(USER_WRITE, USER_READ)),

    ADMIN(Sets.newHashSet(USER_WRITE, USER_READ, ADMIN_READ, ADMIN_WRITE));

    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
