package com.example.Authmodule.dao;

import com.example.Authmodule.entity.AuthUser;
import com.example.Authmodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

import static com.mongodb.client.model.Filters.in;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@Slf4j
public class AuthUserDao {

    private static final String USERS_COLLECTION = "users";
    private final MongoCollection<AuthUser> usersCollection;

    @Autowired
    public AuthUserDao(MongoClient mongoClient,
                       @Value("${spring.mongodb.database}") String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.usersCollection =
                database.getCollection(USERS_COLLECTION, AuthUser.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean insertUser(AuthUser user) {
        try {
            usersCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(user);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'users' collection: {}", user.getEmail(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with email `{0}` already exists.", user.getEmail()));
        }
    }

    public AuthUser findUserByUsername(String username) {
        Bson find_query = in("username", username);
        return Optional.ofNullable(usersCollection.find(find_query).first())
                .orElseThrow(() ->
                        new IncorrectDaoOperation
                                (MessageFormat.format("User with username `{0}` does not exist.", username)));
    }

    public boolean isUserPresent(String username) {
        Bson find_query = in("username", username);
        AuthUser user = usersCollection.find(find_query).first();
        return user != null;
    }
}