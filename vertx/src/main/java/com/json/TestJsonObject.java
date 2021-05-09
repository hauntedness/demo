package com.json;

import io.vertx.core.json.JsonObject;

public class TestJsonObject {
    public static void main(String[] args) {
        String jsonString = "{\"name\":\"tom\"}";
        JsonObject object = new JsonObject(jsonString);
        System.out.println(object.toString());
        String name = object.getString("name");
        System.out.println("name: " + name);
        object.put("name", "Jerry").put("location", "hangzhou");
        System.out.println(object);
    }
}
