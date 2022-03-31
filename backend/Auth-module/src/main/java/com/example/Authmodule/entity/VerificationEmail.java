package com.example.Authmodule.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Document
public class VerificationEmail {
    private String subject;
    private String toEmail;
    private String body;
}
