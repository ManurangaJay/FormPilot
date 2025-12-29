package com.formpilot.activityservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        // This explicitly forces the database name
        return "formpilot_activity_db";
    }

    @Override
    public MongoClient mongoClient() {
        // This explicitly forces the connection string
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/formpilot_activity_db");

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }
}