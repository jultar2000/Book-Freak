package com.example.Ordermodule.order.controller;

import com.example.Ordermodule.order.dto.OrderDto;
import com.example.Ordermodule.order.dto.OrderItemDto;
import com.example.Ordermodule.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/user/{username}/all")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(orderService.findAllOrdersByUser(username));
    }

    @GetMapping("all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("orderId") String orderId) {
        return ResponseEntity.ok(orderService.findOrderDto(orderId));
    }

}
