package com.example.Ordermodule.order.service;

import com.example.Ordermodule.order.dao.OrderDao;
import com.example.Ordermodule.order.entity.Order;
import com.example.Ordermodule.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderDao orderDao;

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

    public void deleteOrder(String id) {
        orderDao.deleteOrder(convertStringIdToObjectId(id));
    }

    public Order findByUserAndOrdered(User user, boolean ordered) {
        return orderDao.findByUserAndOrdered(user, ordered);
    }

    public Order findOrder(String id) {
        return orderDao.findOrder(convertStringIdToObjectId(id));
    }


}
