package com.future;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class TestFuture2 {

    private Future<String> future;

    public TestFuture2() {
        future.onSuccess(
                event -> {
                }
        );
    }

    public Future<String> get() {
        Promise<String> promise = Promise.promise();
        Vertx vertx = Vertx.vertx();
        vertx.setTimer(1000, event -> {
            promise.complete();
        });
        future = promise.future();
        return this.future;
    }

    public Future<String> get2() {
        Promise<String> promise = Promise.promise();
        Vertx vertx = Vertx.vertx();
        vertx.setTimer(1000, event -> {
            promise.complete();
        });
        future = promise.future();
        return this.future;
    }
}
