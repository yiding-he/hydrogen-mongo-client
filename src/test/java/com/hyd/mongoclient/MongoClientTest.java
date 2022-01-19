package com.hyd.mongoclient;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MongoClientTest {

  public static void main(String[] args) throws ScriptException {
    MongoClient client = MongoClients.create("mongodb://10.10.22.152:27017");
    MongoDatabase database = client.getDatabase("admin");

    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("JavaScript");
    Bindings bindings = engine.createBindings();
    bindings.put("db", database);

    Object result = engine.eval("db.listCollectionNames()", bindings);
    System.out.println(result);

    client.close();
  }
}
