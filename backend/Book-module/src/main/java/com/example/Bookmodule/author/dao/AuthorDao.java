package com.example.Bookmodule.author.dao;

import com.example.Bookmodule.author.entity.Author;
import com.example.Bookmodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
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

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class AuthorDao {

    private final Logger log;
    private static final String AUTHORS_COLLECTION = "authors";
    private final MongoCollection<Author> authorsCollection;

    @Autowired
    public AuthorDao(MongoClient mongoClient,
                     @Value("${spring.mongodb.database}") String databaseName) {
        log = LoggerFactory.getLogger(this.getClass());
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.authorsCollection =
                database.getCollection(AUTHORS_COLLECTION, Author.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean insertAuthor(Author author) {
        try {
            authorsCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(author);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'authors' collection: {}", author.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Author with Id `{0}` already exists.", author.getOid()));
        }
    }

    public boolean deleteAuthor(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        try {
            DeleteResult result = authorsCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'authors' collection. No author deleted.", id);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'authors' collection: {1}.", id, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    public Author findAuthor(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        Author author = authorsCollection.find(find_query).first();
        if (author == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Author with Id `{0}` does not exist.", id));
        }
        return author;
    }
}