package com.hyd.mongoclient.controller;

import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.helpers.TableViewHelper;
import com.hyd.mongoclient.MongoClientMain;
import com.hyd.mongoclient.client.MongoClientFactory;
import com.hyd.mongoclient.domain.CollectionInfo;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.hyd.fx.helpers.TableViewHelper.createStrPropColumn;

@Component
@Scope("prototype")
public class TabDatabaseController {

  private static final String COLLECTION_NAME = "collectionName";

  public TableView<CollectionInfo> tbCollectionList;

  public TabPane tpCollections;

  private String databaseName;

  @Autowired
  private MongoClientFactory mongoClientFactory;

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public void initialize() {
    tbCollectionList.getColumns().add(createStrPropColumn("Name", CollectionInfo::nameProperty));

    TableViewHelper.setOnClicked(tbCollectionList, 2, this::createOrOpenCollectionTab);

    mongoClientFactory.getMongoClient().getDatabase(databaseName).listCollectionNames().forEach(collectionName -> {
      CollectionInfo info = new CollectionInfo();
      info.setName(collectionName);
      tbCollectionList.getItems().add(info);
    });
  }

  private void createOrOpenCollectionTab(CollectionInfo collectionInfo) {
    try {
      String collectionName = collectionInfo.getName();
      Tab collectionTab = tpCollections.getTabs().stream()
        .filter(tab -> collectionName.equals(tab.getProperties().get(COLLECTION_NAME)))
        .findFirst().orElse(null);

      if (collectionTab == null) {
        collectionTab = new Tab(collectionName);
        collectionTab.getProperties().put(COLLECTION_NAME, collectionName);
        collectionTab.setContent(MongoClientMain.<TabCollectionController>loadFxml(
          "/fxml/tab_collection.fxml", controller -> {
            controller.setDatabaseName(databaseName);
            controller.setCollectionName(collectionName);
          }));
        tpCollections.getTabs().add(collectionTab);
      }

      tpCollections.getSelectionModel().select(collectionTab);
    } catch (Exception e) {
      AlertDialog.error("错误", e);
    }
  }

  public void createCollectionClicked() {
  }

  public void deleteCollectionClicked() {
  }
}
