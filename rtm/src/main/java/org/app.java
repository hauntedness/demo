package org;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.service.PlotMonitor;
import org.service.SystemMonitor;

import java.util.concurrent.TimeUnit;

public class app {
    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        options.setWarningExceptionTime(5);
        options.setWarningExceptionTimeUnit(TimeUnit.MINUTES);
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(PlotMonitor.class.getName());
        vertx.deployVerticle(SystemMonitor.class.getName());
    }
}
