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
import java.util.List;
import java.util.stream.Collectors;

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

    public List<OrderItemDto> findAllOrderItems() {
        return orderItemDao.findAllOrdersItems()
                .stream()
                .map(orderItem ->
                        mapper.map(orderItem, OrderItemDto.class))
                .collect(Collectors.toList());
    }

    public List<OrderItemDto> findAllOrderItemsByOrder(String orderId) {
        Order order = orderService.findOrder(orderId);
        return orderItemDao.findAllOrdersItemsByOrder(order)
                .stream()
                .map(orderItem ->
                        mapper.map(orderItem, OrderItemDto.class))
                .collect(Collectors.toList());
    }

    public boolean deleteOrderItem(String id) {
        return orderItemDao.deleteOrderItem(convertStringIdToObjectId(id));
    }

    public OrderItem findOrderItem(String id) {
        return orderItemDao.findOrderItem(convertStringIdToObjectId(id));
    }

    public OrderItemDto findOrderItemDto(String id) {
        OrderItem orderItem = orderItemDao.findOrderItem(convertStringIdToObjectId(id));
        return mapper.map(orderItem, OrderItemDto.class);
    }

    /*
        TODO > case when we want to order same order item but with different language/cover not handled.
     */
    public void addOrUpdateOrderItem(OrderItemDto request, String username, String bookId) {
        User user = userService.findUserByUsername(username);
        Order order = orderService.findByUserAndOrdered(user, false);
        Book book = bookService.findBook(convertStringIdToObjectId(bookId));
        OrderItem orderItem = orderItemDao.findOrderItemByOrderAndBook(order, book);
        if (order == null) {
            Order newOrder = Order.builder().ordered(false).user(user).build();
            orderService.insertOrder(newOrder);
            order = newOrder;
        } else if (orderItem != null) {
            int newQuantity = request.getQuantity() == null ? orderItem.getQuantity() + 1 : request.getQuantity();
            orderItemDao.updateOrderItem(orderItem.getOid(), orderItem.getBookCover().name(),
                    orderItem.getBookLanguage().name(), newQuantity);
            return;
        }
        request.setQuantity(request.getQuantity() == null ? 1 : request.getQuantity());
        OrderItem newOrderItem = mapper.map(request, OrderItem.class);
        newOrderItem.setOrder(order);
        newOrderItem.setBook(book);
        orderItemDao.insertOrderItem(newOrderItem);
    }
}
