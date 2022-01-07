package com.hyd.mongoclient;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class
})
public class MongoClientMain {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(MongoClientMain.class, args);
        Application.launch(MongoClientFxApplication.class, args);
    }
}
