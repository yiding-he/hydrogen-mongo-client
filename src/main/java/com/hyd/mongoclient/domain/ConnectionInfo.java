package com.hyd.mongoclient.domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConnectionInfo {

  private final StringProperty name = new SimpleStringProperty();

  private final StringProperty url = new SimpleStringProperty();

  private final StringProperty username = new SimpleStringProperty();

  private final StringProperty password = new SimpleStringProperty();

  public String getName() {
    return name.get();
  }

  public StringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public String getUrl() {
    return url.get();
  }

  public StringProperty urlProperty() {
    return url;
  }

  public void setUrl(String url) {
    this.url.set(url);
  }

  public String getUsername() {
    return username.get();
  }

  public StringProperty usernameProperty() {
    return username;
  }

  public void setUsername(String username) {
    this.username.set(username);
  }

  public String getPassword() {
    return password.get();
  }

  public StringProperty passwordProperty() {
    return password;
  }

  public void setPassword(String password) {
    this.password.set(password);
  }
}
