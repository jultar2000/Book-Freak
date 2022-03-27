package com.example.Bookmodule.book.service;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.book.dao.BookDao;
import com.example.Bookmodule.book.entity.Book;
import com.example.Bookmodule.book.entity.Genre;
import com.example.Bookmodule.exceptions.IncorrectParameterException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class BookService {

    private Logger log;
    private final BookDao bookDao;

    @Autowired
    public BookService(BookDao bookDao) {
        log = LoggerFactory.getLogger(this.getClass());
        this.bookDao = bookDao;
    }

    private ObjectId convertStringIdToObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            log.error("Cannot create ObjectId: {}", e.getMessage());
            String errorMessage = MessageFormat
                    .format("String Id `{0}` cannot be converted to ObjectId.", id);
            throw new IncorrectParameterException(errorMessage);
        }
    }

    public boolean insertBook(Book book, Author author) {
        book.setAuthor(author);
        return bookDao.insertBook(book);
    }

    public boolean deleteBook(String id) {
        return bookDao.deleteBook(convertStringIdToObjectId(id));
    }

    public Book findBook(String id) {
        return bookDao.findBook(convertStringIdToObjectId(id));
    }

    public List<Book> findBooksByKeyword(int limit, int skip, String keyword) {
        return bookDao.findBooksByKeyword(limit, skip, keyword);
    }

    public List<Book> findAllBooks() {
        return bookDao.findAllBooks();
    }

    public List<Book> findBooksByRating(int limit, int skip) {
        return bookDao.findBooksByRating(limit, skip);
    }

    public List<Book> findBooksByAuthor(int limit, int skip, Author author) {
        return bookDao.findBooksByAuthor(limit, skip, author);
    }

    public List<Book> findBooksByGenre(int limit, int skip, String genre) {
        return bookDao.findBooksByGenre(limit, skip, genre);
    }

    public boolean updateBook(String bookId,
                              int numberOfPages,
                              String description,
                              Genre genre) {
        return bookDao.updateBook(
                        convertStringIdToObjectId(bookId),
                        numberOfPages,
                        description,
                        genre);
    }
}