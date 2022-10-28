package com.example.Ordermodule.order.service;

import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.book.service.BookService;
import com.example.Ordermodule.order.dao.OrderDao;
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

    private final OrderDao orderDao;

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
        Order order = orderDao.findOrder(convertStringIdToObjectId(orderId));
        ObjectId orderOid = order.getOid();
        return orderItemDao.findAllOrdersItemsByOrderId(orderOid)
                .stream()
                .map(orderItem ->
                        mapper.map(orderItem, OrderItemDto.class))
                .collect(Collectors.toList());
    }

    public List<OrderItemDto> findAllOrderItemsByActiveOrder(String username) {
        User user = userService.findUserByUsername(username);
        Order order = orderDao.findByUserAndOrdered(user, false);
        ObjectId orderOid = order.getOid();
        return orderItemDao.findAllOrdersItemsByOrderId(orderOid)
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
        Book book = bookService.findBook(convertStringIdToObjectId(bookId));
        Order order = orderDao.findByUserAndOrdered(user, false);
        ObjectId orderOid = order == null ? null : order.getOid();
        OrderItem orderItem = orderItemDao.findOrderItemByOrderIdAndBook(orderOid, book);
        if (orderOid == null) {
            Order newOrder = Order.builder().ordered(false).user(user).build();
            orderDao.insertOrder(newOrder);
            orderOid = newOrder.getOid();
        } else if (orderItem != null) {
            int newQuantity = request.getQuantity() == null ? orderItem.getQuantity() + 1 : request.getQuantity();
            String bookCover = orderItem.getBookCover() == null ? null : orderItem.getBookCover().name();
            String bookLanguage = orderItem.getBookLanguage() == null ? null : orderItem.getBookLanguage().name();
            orderItemDao.updateOrderItem(orderItem.getOid(), bookCover, bookLanguage, newQuantity);
            return;
        }
        request.setQuantity(request.getQuantity() == null ? 1 : request.getQuantity());
        OrderItem newOrderItem = mapper.map(request, OrderItem.class);
        newOrderItem.setOrderId(orderOid);
        newOrderItem.setBook(book);
        orderItemDao.insertOrderItem(newOrderItem);
    }
}
