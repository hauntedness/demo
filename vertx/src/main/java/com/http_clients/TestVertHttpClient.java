
package com.http_clients;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpVersion;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;


public class TestVertHttpClient {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        WebClientOptions options = new WebClientOptions();
        options.setDefaultPort(443);
        options.setProtocolVersion(HttpVersion.HTTP_2);
        options.setUseAlpn(true);
        options.setDefaultHost("api2.chiaexplorer.com");
        options.setSsl(true);
        options.setVerifyHost(false);
        options.setTrustAll(true);
        options.setUserAgentEnabled(true);
        WebClient client = WebClient.create(vertx, options);
        HttpRequest<Buffer> request = client.get("/coinsForAddress/xch1tph43nrun588pl6cegmns8w54apupmhuhsx9mkk84vnahqzas8eql9r7jq");
        request.putHeader("Origin","https://api2.chiaexplorer.com/coinsForAddress/xch1tph43nrun588pl6cegmns8w54apupmhuhsx9mkk84vnahqzas8eql9r7jq");
        request.putHeader("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.4430.212 Safari/537.36 Edge/18.18363");
        request.putHeader("Content-Type", "text/plain");
        request.send().onSuccess(response -> {
            response.headers();
            System.out.println(response.bodyAsString());
        });
    }
}