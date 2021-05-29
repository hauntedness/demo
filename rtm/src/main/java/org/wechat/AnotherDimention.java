package org.wechat;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.utils.Tuple2;

public class AnotherDimention {
    public static Future<Tuple2<String, String>> getRandomMC(Vertx vertx, String token) {
        Promise<Tuple2<String, String>> promise = Promise.promise();
        WebClientOptions options = new WebClientOptions();
        options.setDefaultHost("api.ixiaowai.cn");
        options.setSsl(true);
        options.setDefaultPort(443);
        options.setVerifyHost(false);
        options.setTrustAll(true);
        options.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.4430.212 Safari/537.36 Edge/18.18363");
        WebClient webClient = WebClient.create(vertx, options);
        webClient.get("/mcapi/mcapi.php").send().onSuccess(
                ar -> {
                    promise.complete(new Tuple2<>(token, ar.followedRedirects().get(0)));
                }
        ).onFailure(
                ar -> promise.complete(new Tuple2<>(token, "http://lorempixel.com/1600/900"))
        );
        return promise.future();
    }
}
