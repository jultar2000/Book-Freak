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

    private Gender gender;

    private boolean isAlive;

    private String nationality;

    private LocalDate birthDate;
}
