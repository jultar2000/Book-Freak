package com.example.Usermodule.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressDto {

    private String oid;

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;

    private boolean defaultChoice;

    private String addressType;

}
