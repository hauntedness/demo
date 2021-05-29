package org;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.shareddata.LocalMap;
import org.service.PlotMonitor;
import org.service.SystemMonitor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class app {
    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        options.setWarningExceptionTime(5);
        options.setWarningExceptionTimeUnit(TimeUnit.MINUTES);
        Vertx vertx = Vertx.vertx();
        LocalMap<Object, Object> main = vertx.sharedData().getLocalMap("args");
        Arrays.stream(args).forEach(
                opt -> {
                    String key = opt.split("=")[0].trim();
                    String value = opt.split("=")[1].trim();
                    if (key.equals("node")) {
                        main.put("node", value);
                    } else if (key.equals("user")) {
                        main.put("user", value);
                    }
                }
        );
        if (main.get("user") == null) {
            main.put("node", "abc");
        }
        if (main.get("node") == null) {
            main.put("node", "P006");
        }
        vertx.deployVerticle(PlotMonitor.class.getName());
        vertx.deployVerticle(SystemMonitor.class.getName());
        // vertx.deployVerticle(RewardMonitor.class.getName());
    }
}
