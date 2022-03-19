package com.hyd.mongoclient.controller;

import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.dialog.DialogBuilder;
import com.hyd.fx.helpers.TableViewHelper;
import com.hyd.mongoclient.MongoClientMain;
import com.hyd.mongoclient.client.MongoClientFactory;
import com.hyd.mongoclient.domain.DatabaseInfo;
import com.hyd.mongoclient.event.ConnectionSelectedEvent;
import com.mongodb.client.MongoClients;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainController {

  public static final String DB_NAME = "dbName";

  private Label defaultTablePlaceHolder;

  public MenuItem mnuOpenConnection;

  public TableView<DatabaseInfo> tbDatabaseList;

  public TableColumn<DatabaseInfo, String> colDbName;

  public TableColumn<DatabaseInfo, String> colCollectionCount;

  public TableColumn<DatabaseInfo, String> colDbSize;

  public TabPane tpDatabases;

  public TextField txtSearchDatabase;

  @Autowired
  private MongoClientFactory mongoClientFactory;

  private List<DatabaseInfo> allDatabases;

  public void initialize() {
    defaultTablePlaceHolder = new Label("Database list is empty");
    tbDatabaseList.setPlaceholder(defaultTablePlaceHolder);

    colDbName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDbName()));
    colCollectionCount.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().collectionCountString()));
    colDbSize.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().dbSizeString()));

    TableViewHelper.setOnClicked(tbDatabaseList, 2, this::createOrOpenDatabaseTab);
  }

  private void createOrOpenDatabaseTab(DatabaseInfo databaseInfo) {
    try {
      String dbName = databaseInfo.getDbName();
      Tab dbTab = tpDatabases.getTabs().stream()
        .filter(tab -> dbName.equals(tab.getProperties().get(DB_NAME)))
        .findFirst().orElse(null);

      if (dbTab == null) {
        dbTab = new Tab(dbName);
        dbTab.setClosable(true);
        dbTab.getProperties().put(DB_NAME, dbName);
        dbTab.setContent(loadDatabaseTabContent(dbName));
        tpDatabases.getTabs().add(dbTab);
      }

      tpDatabases.getSelectionModel().select(dbTab);
    } catch (Exception e) {
      AlertDialog.error("错误", e);
    }
  }

  private Parent loadDatabaseTabContent(String dbName) throws IOException {
    return MongoClientMain.<TabDatabaseController>loadFxml(
      "/fxml/tab_database.fxml", controller -> controller.setDatabaseName(dbName)
    );
  }

  public void openConnectionClicked() {
    new DialogBuilder()
      .title("Manage Connections")
      .body(MainController.class.getResource("/fxml/dlg_connections.fxml"))
      .resizable(true)
      .noDefaultButtons()
      .owner(AppPrimaryStage.getPrimaryStage())
      .controllerFactory(MongoClientMain.applicationContext::getBean)
      .showAndWait();
  }

  @EventListener
  public void onConnectionSelected(ConnectionSelectedEvent event) {
    mongoClientFactory.closeCurrentClient();
    mongoClientFactory.setMongoClient(MongoClients.create(event.getConnectionInfo().getUrl()));
    loadDatabases();
  }

  private void loadDatabases() {
    allDatabases = new ArrayList<>();
    tbDatabaseList.setPlaceholder(new Label("Loading database list..."));
    ObservableList<DatabaseInfo> items = tbDatabaseList.getItems();
    items.clear();

    new Thread(() -> {
      mongoClientFactory.getMongoClient().listDatabases().nameOnly(true).forEach(doc -> {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo.setDbName(doc.getString("name"));
        Platform.runLater(() -> items.add(databaseInfo));
        allDatabases.add(databaseInfo);
      });
      Platform.runLater(() -> tbDatabaseList.setPlaceholder(defaultTablePlaceHolder));
    }).start();
  }

  public void exitClicked() {
    Platform.exit();
  }

  public void txtSearchDatabaseChanged() {
    if (StringUtils.isBlank(txtSearchDatabase.getText())) {
      tbDatabaseList.getItems().setAll(allDatabases);
    } else {
      tbDatabaseList.getItems().setAll(
        allDatabases.stream()
          .filter(db -> db.getDbName().contains(txtSearchDatabase.getText().trim()))
          .collect(Collectors.toList())
      );
    }
  }
}
