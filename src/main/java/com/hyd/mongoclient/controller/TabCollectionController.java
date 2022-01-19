package com.hyd.mongoclient.controller;

import com.hyd.mongoclient.client.MongoClientFactory;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TabCollectionController {

  private String databaseName;

  private String collectionName;

  @Autowired
  private MongoClientFactory mongoClientFactory;

  private MongoCollection<Document> mongoCollection;

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public void setCollectionName(String collectionName) {
    this.collectionName = collectionName;
  }

  public void initialize() {
    this.mongoCollection = mongoClientFactory.getMongoClient().getDatabase(databaseName).getCollection(collectionName);
  }
}
