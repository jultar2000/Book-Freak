package com.example.Authmodule.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RefreshTokenRequest {
    private String refreshToken;
    private String username;
}
