package com;

import com.verticles.PlotMonitor;
import io.vertx.core.Vertx;

public class app {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(PlotMonitor.class.getName());
    }
}
