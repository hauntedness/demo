package com.http_clients;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicateResult;

import java.util.function.Function;

public class TestSSLClients {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx);
        Function<HttpResponse<Void>, ResponsePredicateResult> methodsPredicate =
                resp -> {
                    String methods = resp.getHeader("Access-Control-Allow-Methods");
                    if (methods != null) {
                        if (methods.contains("POST")) {
                            return ResponsePredicateResult.success();
                        }
                    }
                    return ResponsePredicateResult.failure("Does not work");
                };

// Send pre-flight CORS request
        client.get(8080, "myserver.mycompany.com", "/some-uri")
                .putHeader("Origin", "Server-b.com")
                .putHeader("Access-Control-Request-Method", "POST")
                .expect(methodsPredicate)
                .send()
                .onSuccess(res -> {
                    // Process the POST request now
                })
                .onFailure(err ->
                        System.out.println("Something went wrong " + err.getMessage()));
    }
}
