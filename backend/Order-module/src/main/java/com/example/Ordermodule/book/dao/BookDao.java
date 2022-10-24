package com.example.Ordermodule.book.dao;

import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.exception.IncorrectDaoOperation;
import com.example.Ordermodule.user.entity.User;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@Slf4j
public class BookDao {

    private static final String BOOK_COLLECTION = "books";
    private final MongoCollection<Book> booksCollection;

    @Autowired
    public BookDao(MongoClient mongoClient,
                   @Value("${spring.data.mongodb.database}") String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.booksCollection =
                database.getCollection(BOOK_COLLECTION, Book.class).withCodecRegistry(pojoCodecRegistry);
    }

    public Book findBook(ObjectId orderId) {
        Bson find_query = Filters.in("_id", orderId);
        Book book = booksCollection.find(find_query).first();
        if (book == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Book with Id `{0}` does not exist.", orderId));
        }
        return book;
    }

    public Book findBookByIdAndUser(ObjectId orderId, User user) {
        Bson find_query = Filters.and(
                Filters.in("_id", orderId),
                Filters.in("user", user));
        Book book = booksCollection.find(find_query).first();
        if (book == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Book with Id `{0}` does not exist.", orderId));
        }
        return book;
    }

    public boolean insertBook(Book book) {
        try {
            booksCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(book);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'books' collection: {}", book.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Book with Id `{0}` already exists.", book.getOid()));
        }
    }

    public boolean deleteBook(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        try {
            DeleteResult result = booksCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'books' collection. No book deleted.", id);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'books' collection: {1}.", id, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }
}
