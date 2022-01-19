package com.hyd.mongoclient.client;

import com.mongodb.client.MongoClient;
import org.springframework.stereotype.Component;

@Component
public class MongoClientFactory {

  private MongoClient mongoClient;

  public MongoClient getMongoClient() {
    return mongoClient;
  }

  public void setMongoClient(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  public void closeCurrentClient() {
    if (mongoClient != null) {
      mongoClient.close();
    }
  }
}
