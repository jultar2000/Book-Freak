package com.example.Authormodule.dao;

import com.example.Authormodule.entity.Author;
import com.example.Authormodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

import java.awt.print.Book;
import java.text.MessageFormat;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class AuthorDao {

    private final Logger log;
    private static final String USERS_COLLECTION = "authors";
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
                database.getCollection(USERS_COLLECTION, Author.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean insertAuthor(Author author) {
        try {
            authorsCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(author);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'authors' collection: {}", author.getId(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Author with Id `{0}` already exists.", author.getId()));
        }
    }

    public boolean deleteAuthor(ObjectId id) {
        Bson find_query = eq("_id", id);
        try {
            DeleteResult result = authorsCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'authors' collection. No author deleted.", id);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{}` from 'authors' collection: {}.", id, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    public Author findAuthor(ObjectId id) {
        Bson find_query = eq("_id", id);
        Author author = authorsCollection.find(find_query).first();
        if (author == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with Id `{0}` does not exist.", id));
        }
        return author;
    }

    public boolean updateAuthorFields(ObjectId id, boolean isAlive) {
        Bson find_query = eq("_id", id);
        Bson update = Updates.combine();

        try {
            UpdateResult updateResult = authorsCollection.updateOne(find_query, update);
            if (updateResult.getModifiedCount() < 1) {
                log.warn(
                        "Author `{}` was not updated. Some field might not exist.",
                        id);
                return false;
            }
        } catch (MongoWriteException e) {
            String errorMessage =
                    MessageFormat.format(
                            "Issue caught while trying to update author `{}`: {}",
                            id,
                            e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
        return true;
    }
}
