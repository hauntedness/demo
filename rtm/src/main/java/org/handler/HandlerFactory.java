package org.handler;

import io.vertx.core.Handler;

public class HandlerFactory {

    public static int i = 0;

    private static Handler<Void> yield = new Handler<Void>() {
        @Override
        public void handle(Void event) {
            i += 1;
            if (Math.floorMod(i, 10000000) == 1) {
                System.out.println(System.currentTimeMillis());
            }
        }
    };

    public static Handler<Void> yieldHandler() {
        return yield;
    }

}
