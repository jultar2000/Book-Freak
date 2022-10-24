package com.example.Ordermodule.order.service;

import com.example.Ordermodule.order.dao.OrderDao;
import com.example.Ordermodule.order.dto.OrderDto;
import com.example.Ordermodule.order.dto.OrderItemDto;
import com.example.Ordermodule.order.entity.Order;
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
public class OrderService {

    private final OrderDao orderDao;

    private final ModelMapper mapper;

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

    public void insertOrder(Order order) {
        orderDao.insertOrder(order);
    }

    public boolean deleteOrder(String orderId) {
        return orderDao.deleteOrder(convertStringIdToObjectId(orderId));
    }

    public boolean updateOrder(String orderId, OrderDto orderDto) {
        return orderDao.updateOrder(convertStringIdToObjectId(orderId),
                orderDto.isOrdered(), orderDto.getShippingStatus());
    }

    public List<OrderDto> findAllOrdersByUser(String username) {
        User user = userService.findUserByUsername(username);
        return orderDao.findAllOrdersByUser(user)
                .stream()
                .map(order ->
                        mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public List<OrderDto> findAllOrders() {
        return orderDao.findAllOrders()
                .stream()
                .map(order ->
                        mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public OrderDto findByUsernameAndOrdered(String username, boolean ordered) {
        User user = userService.findUserByUsername(username);
        Order order = orderDao.findByUserAndOrdered(user, ordered);
        return mapper.map(order, OrderDto.class);
    }

    public Order findByUserAndOrdered(User user, boolean ordered) {
        return orderDao.findByUserAndOrdered(user, ordered);
    }

    public OrderDto findOrderDto(String id) {
        Order order = orderDao.findOrder(convertStringIdToObjectId(id));
        return mapper.map(order, OrderDto.class);
    }

    public Order findOrder(String id) {
        return orderDao.findOrder(convertStringIdToObjectId(id));
    }
}
