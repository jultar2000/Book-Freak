package com.example.Authmodule.entity;

import com.example.Authmodule.security.Role;
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
public class AuthUser {

    @BsonId
    private ObjectId oid;

    private String email;

    private String username;

    private String password;

    private boolean enabled;

    private Role role;
}
