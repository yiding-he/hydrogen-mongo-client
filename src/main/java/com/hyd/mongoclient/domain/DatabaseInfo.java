package com.hyd.mongoclient.domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class DatabaseInfo {

  private String dbName;

  private final IntegerProperty collectionCount = new SimpleIntegerProperty(-1);

  private final LongProperty dbSize = new SimpleLongProperty(-1);

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public int getCollectionCount() {
    return collectionCount.get();
  }

  public IntegerProperty collectionCountProperty() {
    return collectionCount;
  }

  public void setCollectionCount(int collectionCount) {
    this.collectionCount.set(collectionCount);
  }

  public long getDbSize() {
    return dbSize.get();
  }

  public LongProperty dbSizeProperty() {
    return dbSize;
  }

  public void setDbSize(long dbSize) {
    this.dbSize.set(dbSize);
  }

  public String collectionCountString() {
    int count = getCollectionCount();
    return count == -1 ? "-" : String.valueOf(count);
  }

  public String dbSizeString() {
    long dbSize = getDbSize();
    return dbSize == -1 ? "-" : String.format("%,d", dbSize);
  }
}
