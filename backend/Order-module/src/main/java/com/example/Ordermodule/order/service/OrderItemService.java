package com.example.Ordermodule.order.service;

import com.example.Ordermodule.order.entity.Order;
import com.example.Ordermodule.order.entity.OrderItem;
import com.example.Ordermodule.user.entity.User;
import com.example.Ordermodule.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemService {

    private final OrderService orderService;

    private final UserService userService;

    private ObjectId convertStringIdToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            log.error("Cannot create ObjectId: {}", e.getMessage());
            String errorMessage = MessageFormat
                    .format("String Id `{0}` cannot be converted to ObjectId.", id);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void addOrderItemToCart(OrderItem orderItem, String username) {
        User user = userService.findUserByUsername(username);
        if (orderService.findByUserAndOrdered(user, false) != null) {
            Order order = Order.builder().ordered(false).user(user).build();
            orderService.insertOrder(order);
        }
    }
}
