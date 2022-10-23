package com.example.Authmodule.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
public class UserDto {

    private ObjectId objectId;

    private String username;
}
