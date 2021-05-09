package com.future;

import io.vertx.core.Future;
import io.vertx.core.Promise;

public class TestFuture1 {
    private static Promise<String> newPromise() {
        Promise<String> promise = Promise.promise();
        System.out.println("future1");
        return promise;
    }

    public static void main(String[] args) {
        Future<String> future1 = Future.future(
                promise -> {
                    System.out.println("future2");
                    promise.complete("future2 result");
                }
        );


        Future<String> future2 = Future.future(promise -> {
            System.out.println("future2");
            promise.complete("future2 result");
        });

        for (int i = 0; i < 5; i++) {
            Future<String> future = future2.onSuccess(event1 -> {
                System.out.println(event1);
            });

            future2.onComplete(event -> {
                System.out.println(event.result());
            });
        }
    }


}
