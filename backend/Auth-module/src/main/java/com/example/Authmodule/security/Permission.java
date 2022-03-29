package com.example.Authmodule.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    ADMIN_WRITE("admin:write"),
    ADMIN_READ("admin:read"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;
}