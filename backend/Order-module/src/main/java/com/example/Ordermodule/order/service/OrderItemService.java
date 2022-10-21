package com.example.Ordermodule.order.service;

import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.book.service.BookService;
import com.example.Ordermodule.order.dao.OrderItemDao;
import com.example.Ordermodule.order.dto.OrderItemDto;
import com.example.Ordermodule.order.entity.Order;
import com.example.Ordermodule.order.entity.OrderItem;
import com.example.Ordermodule.user.entity.User;
import com.example.Ordermodule.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemService {

    private final OrderItemDao orderItemDao;

    private final OrderService orderService;

    private final UserService userService;

    private final BookService bookService;

    private final ModelMapper mapper;

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

    public void addOrderItemToCart(OrderItemDto request, String username) {
        User user = userService.findUserByUsername(username);
        Order order = orderService.findByUserAndOrdered(user, false);
        if (order == null) {
            Order newOrder = Order.builder().ordered(false).user(user).build();
            orderService.insertOrder(newOrder);
        } else {
            OrderItem orderItem = orderItemDao
                    .findOrderItemByIdAndUser(convertStringIdToObjectId(request.getBookId()), user);
            if (orderItem != null) {
                orderItemDao.updateOrderItem(orderItem.getOid(), orderItem.getQuantity() + 1);

            } else {
                OrderItem newOrderItem = mapper.map(request, OrderItem.class);
                newOrderItem.setOrder(order);
                Book book = bookService.findBook(convertStringIdToObjectId(request.getBookId()));
                newOrderItem.setBook(book);
                orderItemDao.insertOrderItem(newOrderItem);
            }
        }
    }
}
