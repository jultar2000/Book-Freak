package com.example.Ordermodule.order.entity;

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
public class OrderItem {

    @BsonId
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId oid;

    private Order order;

    private int quantity;

    private BookCover bookCover;

    private BookLanguage bookLanguage;

}
