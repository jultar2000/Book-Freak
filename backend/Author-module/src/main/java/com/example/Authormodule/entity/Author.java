package com.example.Authormodule.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Data
public class Author {
    private String name;
    private String surname;
    private LocalDate birthDate;
    private boolean isAlive;



}
