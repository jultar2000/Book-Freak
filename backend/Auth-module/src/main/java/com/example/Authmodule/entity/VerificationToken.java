package com.example.Authmodule.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Document
public class VerificationToken {

    @BsonId
    private ObjectId oid;

    private String token;

    private Instant expiryDate;

    private AuthUser user;
}
