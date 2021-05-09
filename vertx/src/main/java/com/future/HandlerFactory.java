package com.future;

import io.vertx.core.Handler;

public class HandlerFactory {

    public static int i = 0;

    private static Handler<Void> yield = new Handler<Void>() {
        @Override
        public void handle(Void event) {
        }
    };

    public static Handler<Void> yieldHandler() {
        return yield;
    }

}
