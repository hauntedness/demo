package org.wechat;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.common.Session;
import org.utils.Logger;
import org.utils.Properties;

public class WechatMessager {

    private final Vertx vertx;

    public WechatMessager(Vertx vertx) {
        this.vertx = vertx;
    }

    public Future<String> sendMsg(String token, String text) {
        WebClient client = WebClient.create(vertx, Session.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Session.router.send_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        String msgTo = Properties.with("application.properties").get("msgTo");
        json.put("touser", msgTo).put("msgtype", "text").put("agentid", Session.AGENT_ID).put("text", new JsonObject().put("content", text));
        Logger.logger.info("start sendMsg: " + text);
        request.sendJsonObject(json, ar -> {
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                promise.complete(errmsg);
                Logger.logger.info("response from sendMsg: " + ar.result().bodyAsString());
            } else {
                Logger.logger.error("error when sendMsg: " + ar.cause().getMessage());
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }

    public Future<String> getToken() {
        Promise<String> promise = Promise.promise();
        WebClient client = WebClient.create(vertx, Session.options);
        String uri = Session.router.get_token_uri;
        client.get(uri).send(
                ar -> {
                    if (ar.succeeded()) {
                        JsonObject entries = ar.result().bodyAsJsonObject();
                        promise.complete(entries.getString("access_token"));
                    } else {
                        Logger.logger.error("error when getToken: " + ar.cause().getMessage());
                        promise.fail(ar.cause());
                    }
                }
        );
        return promise.future();
    }
}
