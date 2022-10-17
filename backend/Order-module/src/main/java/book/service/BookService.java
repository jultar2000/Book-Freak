package book.service;

import book.dao.BookDao;
import book.entity.Book;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public boolean insertBoot(ObjectId oid) {
        Book book = Book.builder().oid(oid).build();
        return bookDao.insertBook(book);
    }

    public boolean deleteBook(String id) {
        return bookDao.deleteBook(convertStringIdToObjectId(id));
    }

}
