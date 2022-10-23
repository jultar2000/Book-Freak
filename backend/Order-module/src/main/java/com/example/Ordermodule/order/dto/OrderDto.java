package com.example.Ordermodule.order.dto;

import com.example.Ordermodule.order.entity.ShippingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderDto {

    private String oid;

    private boolean ordered;

    private LocalDate orderDate;

    private ShippingStatus shippingStatus;

}
