package com.example.Bookmodule.book.service;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.book.dao.BookDao;
import com.example.Bookmodule.book.entity.Book;
import com.example.Bookmodule.book.entity.ReaderRating;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private final Logger log;

    private final BookDao bookDao;

    private final static int NUMBER_OF_BOOKS_RETURNED = 10;

//    @Autowired
//    public BookService(BookDao bookDao) {
//        log = LoggerFactory.getLogger(this.getClass());
//        this.bookDao = bookDao;
//    }

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

    public boolean insertBook(Book book, Author author) {
        ReaderRating readerRating = ReaderRating.builder()
                .numReviews(0)
                .rating(0)
                .lastUpdated(new Date())
                .build();
        book.setAuthor(author);
        book.setViewerRating(readerRating);
        return bookDao.insertBook(book);
    }

    public boolean deleteBook(String id) {
        return bookDao.deleteBook(convertStringIdToObjectId(id));
    }

    public Book findBook(String id) {
        return bookDao.findBook(convertStringIdToObjectId(id));
    }

    public List<Book> findBooksByKeyword(String keyword) {
        return bookDao.findBooksByKeyword(NUMBER_OF_BOOKS_RETURNED, keyword);
    }

    public List<Book> findAllBooks() {
        return bookDao.findAllBooks();
    }

    public List<Book> findBooksByRating() {
        return bookDao.findBooksByRating(NUMBER_OF_BOOKS_RETURNED);
    }

    public List<Book> findBooksByAuthor(Author author) {
        return bookDao.findBooksByAuthor(NUMBER_OF_BOOKS_RETURNED, author);
    }

    public List<Book> findBooksByGenre(String genre) {
        return bookDao.findBooksByGenre(NUMBER_OF_BOOKS_RETURNED, genre);
    }

    public boolean updateRating(String bookId, double rating) {
        Book book = bookDao.findBook(convertStringIdToObjectId(bookId));
        int new_reviews_num = book.getViewerRating().getNumReviews() + 1;
        if (new_reviews_num > 10 || new_reviews_num < 0) {
            log.warn("Wrong rating number!");
            return false;
        }
        ReaderRating readerRating = ReaderRating.builder()
                .numReviews(new_reviews_num)
                .rating((book.getViewerRating().getRating() + rating) / new_reviews_num)
                .lastUpdated(new Date())
                .build();
        return bookDao.updateRating(convertStringIdToObjectId(bookId), readerRating);
    }

    public boolean updateBook(String bookId,
                              int numberOfPages,
                              String description,
                              String genre) {
        return bookDao.updateBook(convertStringIdToObjectId(bookId),
                numberOfPages,
                description,
                genre);
    }
}
