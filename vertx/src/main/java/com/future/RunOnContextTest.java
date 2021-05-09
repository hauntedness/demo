package com.future;

import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

public class RunOnContextTest {

    private Vertx vertx;
    private final Context context;
    private String event;

    public RunOnContextTest(Vertx vertx) {
        this.event = "";
        this.vertx = vertx;
        this.context = vertx.getOrCreateContext();

    }

    public Context getContext() {
        return this.context;
    }

    public String init(Context context) {
        Promise<Void> promise = Promise.promise();
        WebClient webClient = WebClient.create(vertx);
        HttpRequest<Buffer> req = webClient.getAbs("http://www.baidu.com");
        req.send(ar -> {
                    if (ar.succeeded()) {
                        promise.complete();
                        Context context2 = this.getContext();
                        context2.put("init", "success");
                        System.out.println(context2.get("init").toString());
                    }
                }
        );
        return "";
    }


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        RunOnContextTest test = new RunOnContextTest(vertx);
        Context context = test.getContext();
        test.init(context);
        context.put("init", "");
        while (!"success".equals(context.get("init").toString())) {
            vertx.runOnContext(HandlerFactory.yieldHandler());
        }
        System.out.println("aaaaaa");
    }
}
