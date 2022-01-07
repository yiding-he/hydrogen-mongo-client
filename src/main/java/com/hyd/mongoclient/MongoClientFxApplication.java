package com.hyd.mongoclient;

import com.hyd.fx.Fxml;
import com.hyd.fx.app.AppPrimaryStage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MongoClientFxApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppPrimaryStage.setPrimaryStage(primaryStage);

        FXMLLoader fxmlLoader = Fxml.createFXMLLoader(
            "/fxml/main.fxml", null, MongoClientMain.applicationContext::getBean
        );

        primaryStage.setTitle("Hydrogen MongoDB Client");
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.show();
    }
}
