package com.hyd.mongoclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.function.Consumer;

@SpringBootApplication(exclude = {
  MongoAutoConfiguration.class
})
public class MongoClientMain {

  public static ApplicationContext applicationContext;

  public static void main(String[] args) {
    applicationContext = SpringApplication.run(MongoClientMain.class, args);
    Application.launch(MongoClientFxApplication.class, args);
  }

  @SuppressWarnings("unchecked")
  public static <C> Parent loadFxml(String fxmlPath, Consumer<C> controllerInitializer) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(MongoClientMain.class.getResource(fxmlPath));
    fxmlLoader.setControllerFactory(type -> {
      C controller = (C) applicationContext.getBean(type);
      controllerInitializer.accept(controller);
      return controller;
    });
    return fxmlLoader.load();
  }
}
