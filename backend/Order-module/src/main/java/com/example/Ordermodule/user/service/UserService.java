package com.example.Ordermodule.user.service;

import com.example.Ordermodule.book.dao.BookDao;
import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.order.entity.Order;
import com.example.Ordermodule.user.dao.UserDao;
import com.example.Ordermodule.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

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

    public boolean insertUser(ObjectId oid, String username) {
        User user = User.builder().oid(oid).username(username).build();
        return userDao.insertUser(user);
    }

    public User findUser(String id) {
        return userDao.findUser(convertStringIdToObjectId(id));
    }

    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    public boolean deleteUser(String id) {
        return userDao.deleteUser(convertStringIdToObjectId(id));
    }

}
