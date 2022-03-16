package com.example.Usermodule.dao;

import com.example.Usermodule.entity.User;
import com.example.Usermodule.exceptions.IncorrectDaoOperation;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class UserDao {

    private final Logger log;
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private static final String USERS_COLLECTION = "users";
    private MongoCollection<User> usersCollection;
    private CodecRegistry pojoCodecRegistry;

    @Autowired
    public UserDao(MongoClient mongoClient,
                   @Value("${spring.mongodb.database}") String databaseName) {
        this.mongoClient = mongoClient;
        this.database = this.mongoClient.getDatabase(databaseName);
        log = LoggerFactory.getLogger(this.getClass());
        this.pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.usersCollection =
                this.database.getCollection(USERS_COLLECTION, User.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean insertUser(User user) {
        if (user.getEmail() == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User email cannot be null", user.getEmail()));
        }
        try {
            usersCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(user);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'users' collection: {}", user.getEmail(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with email `{0}` already exists.", user.getEmail()));
        }
    }

    public boolean deleteUser(String email) {
        Bson find_query = eq("email", email);
        try {
            DeleteResult result = usersCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Email '{}' not found in 'users' collection. No user deleted.", email);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{}` from 'users' collection: {}.", email, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    public User findUser(String email) {
        Bson find_query = eq("email", email);
        User user = usersCollection.find(find_query).first();
        if(user == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with email `{0}` does not exist.", email));
        }
        return user;
    }

    public List<String> findAllEmails() {
        List<String> emails = new ArrayList<>();
        usersCollection.distinct("email", String.class).into(emails);
        return emails;
    }

    public boolean updateUserField(String email, String field, String value) {
        Bson find_query = eq("email", email);
        Bson update = set(field, value);
        try {
            UpdateResult updateResult = usersCollection.updateOne(find_query, update);
            if (updateResult.getModifiedCount() < 1) {
                log.warn(
                        "User `{}` was not updated. Field `{}` might not exist.",
                        email, field);
                return false;
            }
        } catch (MongoWriteException e) {
            String errorMessage =
                    MessageFormat.format(
                            "Issue caught while trying to update user `{}`: {}",
                            email,
                            e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
        return true;
    }
}