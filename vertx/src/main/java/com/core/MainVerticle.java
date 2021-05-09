package com.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


public class MainVerticle extends AbstractVerticle implements Verticle {

    public MainVerticle() {
        System.out.println(this.context);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start() throws Exception {
        System.out.println(this.context);
        LOGGER.info(MainVerticle.class.getSimpleName() + " started");
        System.out.println(this.config());
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info(MainVerticle.class.getSimpleName() + " stopped");

    }


    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        MainVerticle mainVerticle = new MainVerticle();
        vertx.deployVerticle(mainVerticle);

        System.out.println(vertx.getOrCreateContext().deploymentID());
        // System.out.println(mainVerticle.deploymentID());
        // vertx.undeploy(mainVerticle.deploymentID());

        vertx.close();
    }

}
