package com.example.Usermodule.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoDBConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MongoClient mongoClient(@Value("${spring.mongodb.uri}") String connectionString) {

        ConnectionString connString = new ConnectionString(connectionString);

        WriteConcern writeConcern = WriteConcern.MAJORITY.withWTimeout(2500, TimeUnit.MILLISECONDS);

        MongoClientSettings settings = MongoClientSettings
                .builder()
                .writeConcern(writeConcern)
                .applyConnectionString(connString)
                .build();

        return MongoClients.create(settings);
    }
}