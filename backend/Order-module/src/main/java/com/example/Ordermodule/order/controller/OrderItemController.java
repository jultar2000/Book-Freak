package com.example.Ordermodule.order.controller;

import com.example.Ordermodule.order.dto.OrderItemDto;
import com.example.Ordermodule.order.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
@AllArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderItemDto>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemService.findAllOrderItems());
    }

    @GetMapping("/all/orders/{Id}")
    public ResponseEntity<List<OrderItemDto>> getAllOrderItemsByOrder(@PathVariable("Id") String id) {
        return ResponseEntity.ok(orderItemService.findAllOrderItemsByOrder(id));
    }

    @GetMapping("/active/users/{username}")
    public ResponseEntity<List<OrderItemDto>> getAllOrderItemsByActiveOrder(@PathVariable("username") String username) {
        return ResponseEntity.ok(orderItemService.findAllOrderItemsByActiveOrder(username));
    }

    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable("orderItemId") String orderItemId) {
        return ResponseEntity.ok(orderItemService.findOrderItemDto(orderItemId));
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable("orderItemId") String orderItemId) {
        if (!orderItemService.deleteOrderItem(orderItemId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/users/{username}/books/{bookId}")
    public ResponseEntity<Void> addItemToCart(@PathVariable("username") String username,
                                              @PathVariable("bookId") String bookId,
                                              @RequestBody OrderItemDto request) {
        orderItemService.addOrUpdateOrderItem(request, username, bookId);
        return ResponseEntity.ok().build();
    }
}
