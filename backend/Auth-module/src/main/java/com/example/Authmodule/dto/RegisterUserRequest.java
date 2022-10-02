package com.example.Authmodule.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class RegisterUserRequest {

    private String username;

    private String password;

    private String email;
}
