package com.example.Ordermodule.user.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {
    @BsonId
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId oid;
}
