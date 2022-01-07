package com.hyd.mongoclient.controller;

import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.dialog.DialogBuilder;
import com.hyd.mongoclient.MongoClientMain;
import javafx.scene.control.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    public MenuItem mnuOpenConnection;

    public void openConnectionClicked() {
        new DialogBuilder()
            .title("Manage Connections")
            .body("/fxml/dlg_connections.fxml")
            .resizable(true)
            .noDefaultButtons()
            .owner(AppPrimaryStage.getPrimaryStage())
            .controllerFactory(MongoClientMain.applicationContext::getBean)
            .showAndWait();
    }
}
