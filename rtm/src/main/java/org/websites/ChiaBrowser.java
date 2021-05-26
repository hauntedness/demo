package org.websites;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.utils.Logger;

public class ChiaBrowser {

    private WebClient client;

    public static ChiaBrowser with(Vertx vertx) {
        ChiaBrowser browser = new ChiaBrowser();
        WebClientOptions options = new WebClientOptions();
        options.setDefaultPort(443);
        options.setProtocolVersion(HttpVersion.HTTP_2);
        options.setUseAlpn(true);
        options.setDefaultHost("api2.chiaexplorer.com");
        options.setSsl(true);
        options.setVerifyHost(false);
        options.setTrustAll(true);
        options.setUserAgentEnabled(true);
        browser.client = WebClient.create(vertx, options);
        return browser;
    }

    public Future<JsonObject> cornsForAddress(String address) {
        Promise<JsonObject> promise = Promise.promise();
        HttpRequest<Buffer> request = this.client.get("/coinsForAddress/" + address);
        request.putHeader("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.4430.212 Safari/537.36 Edge/18.18363");
        request.putHeader("Content-Type", "text/plain");
        request.send().onSuccess(response -> {
            if (response.statusCode() == 200) {
                promise.complete(response.bodyAsJsonObject());
            } else {
                Logger.logger.info(response.statusMessage());
                promise.fail("exception when get coins info from: " + address);
            }
        }).onFailure(cause -> {
            Logger.logger.info(cause.getMessage());
            promise.fail("exception when get coins info from: " + address);
        });
        return promise.future();
    }
}
