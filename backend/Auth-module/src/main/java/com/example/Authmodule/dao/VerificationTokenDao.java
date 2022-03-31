package com.example.Authmodule.dao;

import com.example.Authmodule.entity.VerificationToken;
import com.example.Authmodule.exceptions.IncorrectDaoOperation;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Component
@Slf4j
public class VerificationTokenDao {

    private static final String VERIFICATION_TOKEN_COLLECTION = "verification_tokens";
    private final MongoCollection<VerificationToken> verificationTokenCollection;

    @Autowired
    public VerificationTokenDao(MongoClient mongoClient,
                           @Value("${spring.mongodb.database}") String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.verificationTokenCollection =
                database.getCollection(VERIFICATION_TOKEN_COLLECTION, VerificationToken.class).withCodecRegistry(pojoCodecRegistry);
    }

    public void insertToken(VerificationToken verificationToken) {
        try {
            verificationTokenCollection.withWriteConcern(WriteConcern.MAJORITY).insertOne(verificationToken);
        } catch (MongoWriteException e) {
            log.error("Could not insert `{}` into 'verification_tokens' collection: {}",
                    verificationToken.getToken(), e.getMessage());
            throw new IncorrectDaoOperation(
                    MessageFormat.format("Refresh token `{0}` already exists.", verificationToken.getToken()));
        }
    }

    public Optional<VerificationToken> findByToken(String token) {
        Bson find_query = Filters.in("token", token);
        return Optional.ofNullable(verificationTokenCollection.find(find_query).first());
    }

    public void deleteByToken(String token) {
        Bson find_query = Filters.in("token", token);
        try {
            DeleteResult result = verificationTokenCollection.deleteOne(find_query);
            if (result.getDeletedCount() < 1) {
                log.warn("Id '{}' not found in 'refresh_tokens' collection. No refresh token deleted.", token);
            }
        } catch (Exception e) {
            String errorMessage =
                    MessageFormat.format(
                            "Could not delete `{0}` " +
                                    "from 'refresh_tokens' collection: {1}.", token, e.getMessage());
            throw new IncorrectDaoOperation(errorMessage);
        }
    }
}