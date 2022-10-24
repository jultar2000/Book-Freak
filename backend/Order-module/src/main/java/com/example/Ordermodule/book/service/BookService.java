package com.example.Ordermodule.book.service;

import com.example.Ordermodule.book.dao.BookDao;
import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.exception.IncorrectDaoOperation;
import com.example.Ordermodule.order.entity.Order;
import com.example.Ordermodule.user.entity.User;
import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookDao bookDao;

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

    public Book findBookByIdAndUser(ObjectId orderId, User user) {
        return bookDao.findBookByIdAndUser(orderId, user);
    }

    public Book findBook(ObjectId orderId) {
       return bookDao.findBook(orderId);
    }

    public boolean insertBook(ObjectId oid) {
        Book book = Book.builder().oid(oid).build();
        return bookDao.insertBook(book);
    }

    public boolean deleteBook(String id) {
        return bookDao.deleteBook(convertStringIdToObjectId(id));
    }

}
