package com.example.Usermodule.dto;

import com.example.Usermodule.entity.AddressType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
@Getter
public class GetAddressDto {

    private ObjectId oid;

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;

    private boolean defaultChoice;

    private AddressType addressType;
}
