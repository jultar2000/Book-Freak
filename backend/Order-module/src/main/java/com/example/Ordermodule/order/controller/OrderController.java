package com.example.Ordermodule.order.controller;

import com.example.Ordermodule.order.dto.OrderDto;
import com.example.Ordermodule.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("all/user/{username}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(orderService.findAllOrdersByUser(username));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("orderId") String orderId) {
        return ResponseEntity.ok(orderService.findOrderDto(orderId));
    }

    @GetMapping("/active/users/{username}")
    public ResponseEntity<OrderDto> getActiveOrder(@PathVariable("username") String username) {
        return ResponseEntity.ok(orderService.findOrderDtoByUsernameAndOrdered(username, false));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Void> updateOrder(@PathVariable("orderId") String orderId,
                                            @RequestBody OrderDto request) {
        if (!orderService.updateOrder(orderId, request)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<Void> makeOrder(@PathVariable("username") String username) {
        orderService.makeOrder(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") String orderId) {
        if (!orderService.deleteOrder(orderId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }
}
