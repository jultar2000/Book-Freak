package com.example.Usermodule.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserDto {

    private String name;

    private String surname;

    private double funds;

    private String gender;

    private String birthDate;
}
