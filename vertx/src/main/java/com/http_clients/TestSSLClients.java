package com.http_clients;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

public class TestSSLClients {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        WebClient webClient = WebClient.create(vertx);
        webClient.get("hello").ssl(true).send(a -> {
            if (a.succeeded()) {
                System.out.println("success");
            } else {
                a.cause().printStackTrace();
            }
        });
    }
}
