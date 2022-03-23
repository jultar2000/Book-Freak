package com.example.Bookmodule.author.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
@Getter
public class CreateAuthorDto {
    private ObjectId oid;
}
