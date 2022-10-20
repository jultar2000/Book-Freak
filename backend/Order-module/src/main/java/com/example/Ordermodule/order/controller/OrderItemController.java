package com.example.Ordermodule.order.controller;

import com.example.Ordermodule.order.dto.OrderItemDto;
import com.example.Ordermodule.order.entity.OrderItem;
import com.example.Ordermodule.order.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-items")
@AllArgsConstructor
public class OrderItemController {


    private final OrderItemService orderItemService;

    private final ModelMapper mapper;

    @PostMapping("/{username}")
    public ResponseEntity<Void> addAddress(@PathVariable("username") String username,
                                           @RequestBody OrderItemDto request) {
        OrderItem orderItem = mapper.map(request, OrderItem.class);
//        if (!orderItemService.addOrderItemToCart(orderItem, username)) {
//            return ResponseEntity.badRequest().build();
//        }
        orderItemService.addOrderItemToCart(orderItem, username);
        return ResponseEntity.ok().build();
    }


}
