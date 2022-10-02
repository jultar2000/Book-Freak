package com.example.Usermodule.dto;

import com.example.Usermodule.entity.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetUserDto {

    private String username;

    private String email;

    private String name;

    private String surname;

    private Gender gender;

    private LocalDate birthDate;
}
