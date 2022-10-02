package com.example.Bookmodule.book.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class GetCommentsDto {

    private String oid;

    private String text;

    private Date date;
}
