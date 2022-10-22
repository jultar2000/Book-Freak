package com.example.Ordermodule.order.dto;

import com.example.Ordermodule.order.entity.BookCover;
import com.example.Ordermodule.order.entity.BookLanguage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String oid;

    private String bookId;

    private BookCover bookCover;

    private BookLanguage bookLanguage;

}
