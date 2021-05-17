
package com.http_clients;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;


public class TestVertHttpClient {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        WebClientOptions options = new WebClientOptions();
        options.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36");
        options.setKeepAlive(false).setHttp2MaxPoolSize(10);
        options.setConnectTimeout(100 * 60 * 5);
        WebClient webClient = WebClient.create(vertx, options);
        //WebClient.wrap(httpClient)
        webClient.get("www.baidu.com", "").send(
                asyncResult -> {

                    if (asyncResult.succeeded()) {
                        System.out.println(asyncResult.result().bodyAsString());
                    } else {
                        System.out.println("failed as:" + asyncResult.cause().getMessage());
                    }
                }
        );

    }
}