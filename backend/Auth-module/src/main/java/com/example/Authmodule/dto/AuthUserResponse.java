package com.example.Authmodule.dto;

import lombok.*;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AuthUserResponse {

    private String authenticationToken;

    private String refreshToken;

    private Instant expiresAt;

    private String username;
}
