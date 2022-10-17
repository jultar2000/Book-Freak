package com.example.Ordermodule.user.dao;

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
public class UserDao {

    private static final String USER_COLLECTION = "users";
    private final MongoCollection<User> userCollection;

    @Autowired
    public UserDao(MongoClient mongoClient,
                   @Value("${spring.data.mongodb.database}") String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.userCollection =
                database.getCollection(USER_COLLECTION, User.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean insertUser(User user) {
        try {
            userCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(user);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'users' collection: {}", user.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with Id `{0}` already exists.", user.getOid()));
        }
    }

    public boolean deleteUser(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        try {
            DeleteResult result = userCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'users' collection. No user deleted.", id);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'users' collection: {1}.", id, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    public User findUser(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        User user = userCollection.find(find_query).first();
        if (user == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with Id `{0}` does not exist.", id));
        }
        return user;
    }
}
