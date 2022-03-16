package com.example.Usermodule.dto;

import com.example.Usermodule.entity.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateUserRequest {
    private String email;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
}