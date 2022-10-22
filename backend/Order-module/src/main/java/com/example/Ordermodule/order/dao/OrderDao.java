package com.example.Ordermodule.order.dao;

import com.example.Ordermodule.exception.IncorrectDaoOperation;
import com.example.Ordermodule.order.entity.Order;
import com.example.Ordermodule.order.entity.OrderItem;
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
import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@Slf4j
public class OrderDao {

    private static final String BOOK_COLLECTION = "orders";
    private final MongoCollection<Order> ordersCollection;

    @Autowired
    public OrderDao(MongoClient mongoClient,
                        @Value("${spring.data.mongodb.database}") String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.ordersCollection =
                database.getCollection(BOOK_COLLECTION, Order.class).withCodecRegistry(pojoCodecRegistry);
    }

    public void insertOrder(Order order) {
        try {
            ordersCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(order);
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'orders' collection: {}", order.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Order with Id `{0}` already exists.", order.getOid()));
        }
    }

    public void deleteOrder(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        try {
            DeleteResult result = ordersCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'orders' collection. No order deleted.", id);
            }
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'orders' collection: {1}.", id, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }

    public Order findByUserAndOrdered(User user, boolean ordered) {
        Bson find_query = Filters.and(
                Filters.in("user", user),
                Filters.in("ordered", ordered));
        Order order = ordersCollection.find(find_query).first();
        return order;
    }

    /**
     * Finds all orders in 'orders' collection owned by user.
     *
     * @param user - user that order should be serached for
     *
     * @return list of found books.
     */
    public List<Order> findAllOrdersByUser(User user) {
        Bson find_query = Filters.in("user", user);
        List<Order> orders = new ArrayList<>();
        ordersCollection
                .find(find_query)
                .into(orders);
        return orders;
    }

    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        ordersCollection
                .find()
                .into(orders);
        return orders;
    }

    public Order findOrder(ObjectId orderId) {
        Bson find_query = Filters.in("_id", orderId);
        Order order = ordersCollection.find(find_query).first();
        if (order == null) {
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Order with Id `{0}` does not exist.", orderId));
        }
        return order;
    }


}
