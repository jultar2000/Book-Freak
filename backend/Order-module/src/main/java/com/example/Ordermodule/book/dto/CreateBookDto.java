package com.example.Ordermodule.book.dto;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
@Getter
public class CreateBookDto {

    private ObjectId oid;

}
