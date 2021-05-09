package org;

import io.vertx.core.Vertx;
import org.service.PlotMonitor;

public class app {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(PlotMonitor.class.getName());
    }
}
