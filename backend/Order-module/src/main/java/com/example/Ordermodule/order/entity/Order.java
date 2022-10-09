package com.example.Ordermodule.order.entity;

import com.example.Ordermodule.user.entity.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
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
public class Order {

    @BsonId
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId oid;

    private User user;

    private boolean ordered;

    private LocalDate orderDate;

    private ShippingStatus shippingStatus;

}
