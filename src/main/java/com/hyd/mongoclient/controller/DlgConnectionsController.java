package com.hyd.mongoclient.controller;

import com.hyd.fx.cells.ListCellFactory;
import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.enhancements.ListViewEnhancements;
import com.hyd.fx.window.WindowHelper;
import com.hyd.mongoclient.domain.ConnectionInfo;
import com.hyd.mongoclient.event.ConnectionSelectedEvent;
import com.hyd.mongoclient.repository.ConnectionsRepository;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Component
public class DlgConnectionsController {

  @Autowired
  private ConnectionsRepository connectionsRepository;

  @Autowired
  private ApplicationEventPublisher publisher;

  public ListView<ConnectionInfo> lvConnections;

  public TextField txtName;

  public TextArea txtConnectionString;

  public TextField txtUsername;

  public TextField txtPassword;

  private boolean creating;

  private void withSelectedConnectionInfo(BiConsumer<Integer, ConnectionInfo> consumer) {
    int index = lvConnections.getSelectionModel().getSelectedIndex();
    if (index >= 0) {
      consumer.accept(index, lvConnections.getItems().get(index));
    }
  }

  public void initialize() {
    lvConnections.setCellFactory(
      new ListCellFactory<ConnectionInfo>().withTextProperty(ConnectionInfo::nameProperty)
    );
    ListViewEnhancements.onSelectionChanged(lvConnections, this::showConnectionInfo);
    reloadList();
  }

  private void reloadList() {
    lvConnections.getItems().setAll(connectionsRepository.all());
  }

  private void showConnectionInfo(ConnectionInfo connectionInfo) {
    this.creating = false;
    if (connectionInfo == null) {
      clearForm();
    } else {
      txtName.setText(connectionInfo.getName());
      txtConnectionString.setText(connectionInfo.getUrl());
      txtUsername.setText(connectionInfo.getUsername());
      txtPassword.setText(connectionInfo.getPassword());
    }
  }

  private void clearForm() {
    txtName.setText(null);
    txtConnectionString.setText(null);
    txtUsername.setText(null);
    txtPassword.setText(null);
  }

  public void closeClicked(ActionEvent actionEvent) {
    WindowHelper.closeContainerWindow(actionEvent.getSource());
  }

  public void createConnectionClicked() {
    this.creating = true;
    clearForm();
  }

  public void deleteConnectionClicked() {
    withSelectedConnectionInfo((i, connectionInfo) -> {
      if (AlertDialog.confirmYesNo("Delete Connection", "Do you want to delete connection ''?")) {
        connectionsRepository.delete(i);
        clearForm();
        reloadList();
      }
    });
  }

  public void openConnectionClicked(ActionEvent actionEvent) {
    withSelectedConnectionInfo((index, connectionInfo) -> {
      WindowHelper.closeContainerWindow(actionEvent.getSource());
      publisher.publishEvent(new ConnectionSelectedEvent(connectionInfo));
    });
  }

  public void testConnectionClicked() {
    withSelectedConnectionInfo((i, connectionInfo) -> {
      try {
        MongoClientSettings settings = MongoClientSettings
          .builder()
          .applyConnectionString(new ConnectionString(connectionInfo.getUrl()))
          .applyToSocketSettings(socket -> socket.connectTimeout(10, TimeUnit.SECONDS))
          .build();

        MongoClient mongoClient = MongoClients.create(settings);
        mongoClient.listDatabaseNames().first();
        mongoClient.close();

        AlertDialog.info("SUCCESS", "Connection is ready for use.");
      } catch (Exception e) {
        AlertDialog.error("ERROR", e);
      }
    });
  }

  public void saveConnectionClicked() {
    int index = lvConnections.getSelectionModel().getSelectedIndex();
    if (creating || index < 0) {
      ConnectionInfo connectionInfo = new ConnectionInfo();
      fillConnectionInfo(connectionInfo);
      connectionsRepository.add(connectionInfo);
      creating = false;
      reloadList();
      lvConnections.getSelectionModel().select(lvConnections.getItems().size() - 1);
    } else {
      ConnectionInfo connectionInfo = lvConnections.getSelectionModel().getSelectedItem();
      fillConnectionInfo(connectionInfo);
      connectionsRepository.update(index, connectionInfo);
    }
  }

  private void fillConnectionInfo(ConnectionInfo connectionInfo) {
    connectionInfo.setName(txtName.getText());
    connectionInfo.setUrl(txtConnectionString.getText());
    connectionInfo.setUsername(txtUsername.getText());
    connectionInfo.setPassword(txtPassword.getText());
  }
}
