package com.example.Ordermodule.order.controller;

import com.example.Ordermodule.order.dto.OrderItemDto;
import com.example.Ordermodule.order.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-items")
@AllArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/{username}")
    public ResponseEntity<Void> addItemToCart(@PathVariable("username") String username,
                                           @RequestBody OrderItemDto request) {
        orderItemService.addOrderItemToCart(request, username);
        return ResponseEntity.ok().build();
    }
}
