package org.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.utils.Logger;
import org.utils.Properties;
import org.websites.ChiaBrowser;
import org.wechat.WechatMessager;

import java.util.Arrays;

public class RewardMonitor extends AbstractVerticle {

    private final Properties properties = Properties.with("application.properties");

    @Override
    public void start() {
        vertx.setPeriodic(Long.parseLong(properties.get("messagePeriod")), event -> {
            WechatMessager messager = new WechatMessager(vertx);
            Future<String> future = messager.getToken();
            future.onSuccess(token -> Arrays.stream(properties.get("coinAddresses").split(","))
                    .forEach(address -> this.getNewIncome(address, token, messager)));
        });
    }

    private void getNewIncome(String address, String token, WechatMessager messager) {
        ChiaBrowser.with(vertx).cornsForAddress(address).onSuccess(
                json -> {
                    Logger.logger.info(json.toString());
                    JsonArray coins = json.getJsonArray("coins");
                    for (int i = 0; i < coins.size(); i++) {
                        JsonObject jsonObject = coins.getJsonObject(i);
                        Long timestamp = Long.parseLong(jsonObject.getString("timestamp"));
                        if (System.currentTimeMillis() - timestamp * 1000 < 1000 * 60 * 60) {
                            messager.sendMsg(token, "new coin !!! address: \n" + address);
                        }
                    }
                }
        );
    }

    @Override
    public void stop() {
    }
}
