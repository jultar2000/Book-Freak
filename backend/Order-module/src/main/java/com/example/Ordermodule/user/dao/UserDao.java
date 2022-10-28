package com.example.Ordermodule.user.dao;

import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.exception.IncorrectDaoOperation;
import com.example.Ordermodule.order.entity.Order;
import com.example.Ordermodule.user.entity.User;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.in;
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

    public boolean updateUserFields(String username,
                                    double funds) {
        Bson find_query = in("username", username);
        List<Bson> updatesList = new ArrayList<>();
        if (funds > 0) {
            updatesList.add(Updates.set("funds", funds));
        }
        Bson update = Updates.combine(updatesList);
        try {
            UpdateResult updateResult = userCollection.updateOne(find_query, update);
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

    public User findUser(ObjectId orderId) {
        Bson find_query = Filters.in("_id", orderId);
        User user = userCollection.find(find_query).first();
        if (user == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with Id `{0}` does not exist.", orderId));
        }
        return user;
    }

    public User findUserByUsername(String username) {
        Bson find_query = Filters.in("username", username);
        User user = userCollection.find(find_query).first();
        if (user == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("User with username `{0}` does not exist.", username));
        }
        return user;
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
}
