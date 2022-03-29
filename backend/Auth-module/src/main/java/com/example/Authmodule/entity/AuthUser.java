package com.example.Authmodule.entity;

import com.example.Authmodule.security.Role;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Document
public class AuthUser {
    private String email;
    private String username;
    private String password;
    private Instant created;
    private boolean enabled;
    private Role role;
}