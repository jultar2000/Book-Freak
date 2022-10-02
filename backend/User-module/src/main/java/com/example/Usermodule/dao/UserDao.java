package com.example.Usermodule.dao;

import com.example.Usermodule.entity.User;
import com.example.Usermodule.exceptions.IncorrectDaoOperation;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.in;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
public class UserDao {

    private final Logger log;
    private static final String USERS_COLLECTION = "users";
    private final MongoCollection<User> usersCollection;

    @Autowired
    public UserDao(MongoClient mongoClient,
                   @Value("${spring.data.mongodb.database}") String databaseName) {
        log = LoggerFactory.getLogger(this.getClass());
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.usersCollection =
                database.getCollection(USERS_COLLECTION, User.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean deleteUser(String username) {
        Bson find_query = in("username", username);
        try {
            DeleteResult result = usersCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Email '{}' not found in 'users' collection. No user deleted.", username);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'users' collection: {1}.", username, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    public User findUser(String username) {
        Bson find_query = in("username", username);
        User user = usersCollection.find(find_query).first();
        if (user == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with email `{0}` does not exist.", username));
        }
        return user;
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        usersCollection
                .find()
                .into(users);
        return users;
    }

    public boolean updateUserFields(String username,
                                    String name,
                                    String surname,
                                    String gender,
                                    String birthDate) {
        Bson find_query = in("username", username);
        List<Bson> updatesList = new ArrayList<>();
        if (name != null) {
            updatesList.add(Updates.set("name", name));
        }
        if (surname != null) {
            updatesList.add(Updates.set("surname", surname));
        }
        if (gender != null) {
            updatesList.add(Updates.set("gender", gender));
        }
        if (birthDate != null) {
            updatesList.add(Updates.set("birthDate", LocalDate.parse(birthDate)));
        }
        Bson update = Updates.combine(updatesList);
        return performUpdate(username, find_query, update);
    }

    public boolean updateUserImage(String username, InputStream is) {
        Bson find_query = in("username", username);
        byte[] byte_stream;
        try {
            byte_stream = is.readAllBytes();
        } catch (IOException ex) {
            String errorMessage =
                    MessageFormat.format(
                            "Wrong byte data stream: {0}",
                            ex.getMessage());
            throw new IllegalStateException(errorMessage);
        }
        Bson update = Updates.set("image", byte_stream);
        return performUpdate(username, find_query, update);
    }

    private boolean performUpdate(String username, Bson find_query, Bson update) {
        try {
            UpdateResult updateResult = usersCollection.updateOne(find_query, update);
            if (updateResult.getModifiedCount() < 1) {
                log.warn(
                        "User `{}` was not updated. User might not exist or all fields remain the same.",
                        username);
                return false;
            }
        } catch (MongoWriteException e) {
            String errorMessage =
                    MessageFormat.format(
                            "Issue caught while trying to update user `{0}`: {1}",
                            username,
                            e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
        return true;
    }
}
