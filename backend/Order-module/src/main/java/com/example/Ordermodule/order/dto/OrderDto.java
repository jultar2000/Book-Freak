package com.example.Ordermodule.order.dto;

import com.example.Ordermodule.order.entity.ShippingStatus;
import com.example.Ordermodule.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderDto {

    private String oid;

    private User user;

    private boolean ordered;

    private LocalDate orderDate;

    private ShippingStatus shippingStatus;

}
