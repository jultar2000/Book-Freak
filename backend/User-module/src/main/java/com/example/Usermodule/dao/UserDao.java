package com.example.Usermodule.dao;

import com.example.Usermodule.entity.User;
import com.example.Usermodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static com.mongodb.client.model.Filters.eq;
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
        Bson query = eq("email", email);
        try {
            usersCollection.deleteOne(query);
            return true;
        } catch(Exception e) {
            log.error("Could not delete `{}` from 'users' collection: {}", email, e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with email `{0}` does not exist.", email));
        }
    }

    public User findUser(String email) {
        Bson query = eq("email", email);
        try {
            return usersCollection.find(query).first();
        } catch(Exception e) {
            log.error("Could not find `{}` in 'users' collection: {}", email, e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with email `{0}` does not exist.", email));
        }
    }
}