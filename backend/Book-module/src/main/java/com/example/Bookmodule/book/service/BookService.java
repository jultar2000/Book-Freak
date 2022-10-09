package com.example.Bookmodule.book.service;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.book.dao.BookDao;
import com.example.Bookmodule.book.dto.GetBookDto;
import com.example.Bookmodule.book.entity.Book;
import com.example.Bookmodule.book.entity.ReaderRating;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookDao bookDao;

    private final static int NUMBER_OF_BOOKS_RETURNED = 10;

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

    public List<GetBookDto> findBooksByKeyword(String keyword) {
        return bookDao.findBooksByKeyword(NUMBER_OF_BOOKS_RETURNED, keyword)
                .stream()
                .map(book ->
                        mapper.map(book, GetBookDto.class))
                .collect(Collectors.toList());
    }

    public List<GetBookDto> findAllBooks() {
        return bookDao.findAllBooks()
                .stream()
                .map(book ->
                        mapper.map(book, GetBookDto.class))
                .collect(Collectors.toList());
    }

    public List<GetBookDto> findBooksByRating() {
        return bookDao.findBooksByRating(NUMBER_OF_BOOKS_RETURNED)
                .stream()
                .map(book ->
                        mapper.map(book, GetBookDto.class))
                .collect(Collectors.toList());
    }

    public List<GetBookDto> findBooksByAuthor(Author author) {
        return bookDao.findBooksByAuthor(NUMBER_OF_BOOKS_RETURNED, author)
                .stream()
                .map(book ->
                        mapper.map(book, GetBookDto.class))
                .collect(Collectors.toList());
    }

    public List<GetBookDto> findBooksByGenre(String genre) {
        return bookDao.findBooksByGenre(NUMBER_OF_BOOKS_RETURNED, genre)
                .stream()
                .map(book ->
                        mapper.map(book, GetBookDto.class))
                .collect(Collectors.toList());
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
