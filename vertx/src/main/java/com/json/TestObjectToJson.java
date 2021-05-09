package com.json;

import io.vertx.core.json.JsonObject;

import java.io.Serializable;

public class TestObjectToJson implements Serializable {

    public static void main(String[] args) {
        //POJO to Json
        MyItem myItem = new MyItem();

        myItem.setName("J");
        myItem.setLocation("Hangzhou");
        JsonObject entries = JsonObject.mapFrom(myItem);
        String name = entries.getString("name", "nameless");
        System.out.println(entries.toString());
        System.out.println("name is:" + name);
    }

}

