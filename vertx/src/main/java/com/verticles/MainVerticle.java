package com.verticles;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(
                req -> req.response().end("hello Vert.x")
        ).listen(8080);
        super.start();
    }
}
