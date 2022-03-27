package com.example.Bookmodule.book.dao;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.book.entity.Book;
import com.example.Bookmodule.book.entity.Comment;
import com.example.Bookmodule.book.entity.Genre;
import com.example.Bookmodule.book.entity.ViewerRating;
import com.example.Bookmodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class BookDao {

    private final Logger log;
    private static final String BOOKS_COLLECTION = "books";
    private final MongoCollection<Book> booksCollection;

    @Autowired
    public BookDao(MongoClient mongoClient,
                   @Value("${spring.mongodb.database}") String databaseName) {
        log = LoggerFactory.getLogger(this.getClass());
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.booksCollection =
                database.getCollection(BOOKS_COLLECTION, Book.class).withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Inserts the 'books' object in the 'books' collection.
     *
     * @param book - Book object to be inserted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean insertBook(Book book) {
        try {
            booksCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(book);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'books' collection: {}", book.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Book with id `{0}` already exists.", book.getOid()));
        }
    }

    /**
     * Deletes the book document from the 'books' collection with the provided bookId.
     *
     * @param bookId - id of the book to be deleted.
     * @return True if successful, throw IncorrectDaoOperation otherwise.
     */
    public boolean deleteBook(ObjectId bookId) {
        Bson find_query = Filters.in("_id", bookId);
        try {
            DeleteResult result = booksCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'books' collection. No books deleted.", bookId);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'books' collection: {1}.", bookId, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    /**
     * Finds all books in 'books' collection.
     *
     * @return list of found books.
     */
    public List<Book> findAllBooks() {
        List<Book> books = new ArrayList<>();
        booksCollection
                .find()
                .into(books);
        return books;
    }

    /**
     * Given the bookId, finds the book object in 'books' collection.
     *
     * @param bookId - id of the book.
     * @return book object, if null throws IncorrectDaoOperation.
     */
    public Book findBook(ObjectId bookId) {
        Bson find_query = Filters.in("_id", bookId);
        Book book = booksCollection
                .find(find_query)
                .first();
        if (book == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Book with Id `{0}` does not exist.", bookId));
        }
        return book;
    }

    /**
     * Performs text search by specified keyword in 'books' collection.
     *
     * @param limit - number of documents to be returned.
     * @param skip - number of documents to be skipped.
     * @param keyword - string word from which text search is performed.
     * @return list of books that match specified criteria.
     */
    public List<Book> findBooksByKeyword(int limit, int skip, String keyword) {
        Bson textFilter = Filters.text(keyword);
        Bson projection = Projections.metaTextScore("score");
        Bson sort = Sorts.metaTextScore("textScore");
        List<Book> books = new ArrayList<>();
        booksCollection
                .find(textFilter)
                .projection(projection)
                .sort(sort)
                .limit(limit)
                .skip(skip)
                .iterator()
                .forEachRemaining(books::add);
        return books;
    }

    /**
     * Finds books in 'books' collection and sorts them by rating in descending order.
     *
     * @param limit - number of documents to be returned.
     * @param skip - number of documents to be skipped.
     * @return list of books sorted by rating.
     */
    public List<Book> findBooksByRating(int limit, int skip) {
        String sortKey = "viewerRating.rating";
        List<Book> books = new ArrayList<>();
        Bson find_query = Sorts.descending(sortKey);
        booksCollection
                .find(find_query)
                .skip(skip)
                .limit(limit)
                .iterator()
                .forEachRemaining(books::add);
        return books;
    }

    /**
     * Given the author object, finds all books in 'books' collection by author.
     *
     * @param limit - number of documents to be returned.
     * @param skip - number of documents to be skipped.
     * @param author - author object.
     * @return list of books that match specified criteria.
     */
    public List<Book> findBooksByAuthor(int limit, int skip, Author author) {
        Bson find_query = Filters.in("author", author);
        List<Book> books = new ArrayList<>();
        booksCollection
                .find(find_query)
                .limit(limit)
                .skip(skip)
                .into(books);
        return books;
    }

    /**
     * Finds all books in 'books' collection by genre.
     *
     * @param limit - number of documents to be returned.
     * @param skip - number of documents to be skipped.
     * @param genre - string value of genre.
     * @return list of books that match specified criteria.
     */
    public List<Book> findBooksByGenre(int limit, int skip, String genre) {
        Bson find_query = Filters.in("genre", genre);
        List<Book> books = new ArrayList<>();
        booksCollection
                .find(find_query)
                .limit(limit)
                .skip(skip)
                .iterator()
                .forEachRemaining(books::add);
        return books;
    }

    /**
     * Given the book's id, finds book object and updates numberOfPages, description and genre fields.
     *
     * @param bookId - id of the book.
     * @param numberOfPages - integer number of pages value.
     * @param description - string description value.
     * @param genre - string genre value.
     * @return true if successful, false if not, throws IncorrectDaoOperation if field cannot be updated.
     */
    public boolean updateBook(ObjectId bookId,
                              int numberOfPages,
                              String description,
                              Genre genre) {
        Bson find_query = Filters.in("_id", bookId);
        Bson update = Updates.combine(
                Updates.set("numberOfPages", numberOfPages),
                Updates.set("description", description),
                Updates.set("genre", genre));
        try {
            UpdateResult updateResult = booksCollection.updateOne(find_query, update);
            if (updateResult.getModifiedCount() < 1) {
                log.warn(
                        "Book `{}` was not updated. Some field might not exist.",
                        bookId);
                return false;
            }
        } catch (MongoWriteException e) {
            String errorMessage =
                    MessageFormat.format(
                            "Issue caught while trying to update author `{}`: {}",
                            bookId,
                            e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
        return true;
    }
}
