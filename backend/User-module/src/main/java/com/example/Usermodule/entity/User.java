package com.example.Usermodule.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Document
public class User {
    private String email;
    private String name;
    private String surname;
    private String gender;
    private LocalDate birthDate;
}