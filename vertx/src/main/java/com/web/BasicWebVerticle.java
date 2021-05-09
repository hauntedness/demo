package com.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class BasicWebVerticle extends AbstractVerticle {

    private static final Logger LOOGER = LoggerFactory.getLogger(BasicWebVerticle.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new BasicWebVerticle());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        LOOGER.info(BasicWebVerticle.class.getSimpleName() + " started");



        vertx.createHttpServer().requestHandler(event -> {
            event.response().end("welcome to " + BasicWebVerticle.class.getSimpleName());
        }).listen(8080);
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        LOOGER.info(BasicWebVerticle.class.getSimpleName() + " stopped, bye bye");
    }
}
