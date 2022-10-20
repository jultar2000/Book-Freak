package com.example.Ordermodule.order.dao;

import com.example.Ordermodule.book.entity.Book;
import com.example.Ordermodule.exception.IncorrectDaoOperation;
import com.example.Ordermodule.order.entity.OrderItem;
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
public class OrderItemDao {

    private static final String BOOK_COLLECTION = "order_items";
    private final MongoCollection<OrderItem> orderItemCollection;

    @Autowired
    public OrderItemDao(MongoClient mongoClient,
                   @Value("${spring.data.mongodb.database}") String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.orderItemCollection =
                database.getCollection(BOOK_COLLECTION, OrderItem.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean insertOrderItem(OrderItem orderItem) {
        try {
            orderItemCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(orderItem);
            return true;
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'order_items' collection: {}", orderItem.getOid(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Order item with Id `{0}` already exists.", orderItem.getOid()));
        }
    }

    public boolean deleteOrderItem(ObjectId id) {
        Bson find_query = Filters.eq("_id", id);
        try {
            DeleteResult result = orderItemCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'order_items' collection. No order item deleted.", id);
            }
            return true;
        } catch (Exception e) {
            String errorMessage = MessageFormat
                    .format("Could not delete `{0}` from 'order_items' collection: {1}.", id, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }


}
