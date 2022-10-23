package com.example.Ordermodule.order.dto;

import com.example.Ordermodule.book.entity.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String oid;

    private Book book;

    private String orderId;

    private Integer quantity;

    private String bookCover;

    private String bookLanguage;

}
