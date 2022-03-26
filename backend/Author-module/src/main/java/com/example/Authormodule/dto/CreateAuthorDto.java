package com.example.Authormodule.dto;

import com.example.Authormodule.entity.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateAuthorDto {
    private String name;
    private String surname;
    private String nationality;
    private Gender gender;
    private LocalDate birthDate;
    private boolean isAlive;
}