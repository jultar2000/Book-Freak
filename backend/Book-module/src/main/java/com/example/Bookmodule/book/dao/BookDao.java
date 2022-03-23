package com.example.Bookmodule.book.dao;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.book.entity.Book;
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
import com.mongodb.client.result.DeleteResult;
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
    private static final String AUTHORS_COLLECTION = "books";
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
                database.getCollection(AUTHORS_COLLECTION, Book.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean insertBook(Book book) {
        try {
            booksCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(book);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'authors' collection: {}", book.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Author with Id `{0}` already exists.", book.getOid()));
        }
    }

    public boolean deleteBook(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        try {
            DeleteResult result = booksCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'books' collection. No books deleted.", id);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'books' collection: {1}.", id, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    public Book findBook(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        Book book = booksCollection.find(find_query).first();
        if (book == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Book with Id `{0}` does not exist.", id));
        }
        return book;
    }

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

    public List<Book> findAllBooks() {
        List<Book> books = new ArrayList<>();
        booksCollection
                .find()
                .into(books);
        return books;
    }

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

    public List<Book> findBooksByAuthor(Author author) {
        Bson find_query = Filters.eq("author", author);
        List<Book> books = new ArrayList<>();
        booksCollection
                .find(find_query)
                .into(books);
        return books;
    }
}
