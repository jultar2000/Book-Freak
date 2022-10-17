package com.example.Usermodule.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Document
public class Address {

    @BsonId
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId oid;

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;

    private boolean defaultChoice;

    private AddressType addressType;

    private User user;

}
