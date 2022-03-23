package com.example.Bookmodule.author.entity;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Author {
    @BsonId
    private ObjectId oid;
}