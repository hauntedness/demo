package org.wechat;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.common.Session;
import org.utils.Logger;
import org.utils.Properties;

public class WechatMessager {

    private final Vertx vertx;
    private final String agent;

    public WechatMessager(Vertx vertx) {
        this.vertx = vertx;
        LocalMap<Object, Object> main = vertx.sharedData().getLocalMap("args");
        agent = Session.getAgent(((String) main.get("node")));
    }

    public Future<String> sendMsg(String token, String text) {
        WebClient client = WebClient.create(vertx, Session.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Session.router.send_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        String msgTo = Properties.with("application.properties").get("msgTo");
        json.put("touser", msgTo).put("msgtype", "text").put("agentid", agent).put("text", new JsonObject().put("content", text));
        Logger.logger.info("sendMsg: " + text);
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

    public Future<String> sendMarkDown(String token, String text) {
        WebClient client = WebClient.create(vertx, Session.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Session.router.send_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        String msgTo = Properties.with("application.properties").get("msgTo");
        json.put("touser", msgTo).put("msgtype", "markdown").put("agentid", agent).put("markdown", new JsonObject().put("content", text));
        Logger.logger.info("sendMarkDown: " + text);
        request.sendJsonObject(json, ar -> {
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                promise.complete(errmsg);
                Logger.logger.info("response from sendMarkDown: " + ar.result().bodyAsString());
            } else {
                Logger.logger.error("error when sendMarkDown: " + ar.cause().getMessage());
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }

    public Future<String> sendTextCard(String token, String title, String description) {
        WebClient client = WebClient.create(vertx, Session.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Session.router.send_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        String msgTo = Properties.with("application.properties").get("msgTo");
        json.put("touser", msgTo)
                .put("msgtype", "textcard")
                .put("agentid", agent)
                .put("textcard", new JsonObject().put("title", title).put("description", description).put("url", "https://github.com/Chia-Network/chia-blockchain"));
        Logger.logger.info("sendTextCard: " + description);
        request.sendJsonObject(json, ar -> {
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                promise.complete(errmsg);
                Logger.logger.info("response from sendTextCard: " + ar.result().bodyAsString());
            } else {
                Logger.logger.error("error when sendTextCard: " + ar.cause().getMessage());
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }


    public Future<String> sendNews(String token, String title, String description, String picURL) {
        WebClient client = WebClient.create(vertx, Session.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Session.router.send_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        String msgTo = Properties.with("application.properties").get("msgTo");
        json.put("touser", msgTo)
                .put("msgtype", "news")
                .put("agentid", agent)
                .put("news", new JsonObject().put("articles", new JsonArray()
                        .add(new JsonObject().put("title", title)
                                .put("description", description)
                                .put("url", "https://api2.chiaexplorer.com")
                                .put("picurl", picURL))));
        Logger.logger.info("sendNews: " + description);
        request.sendJsonObject(json, ar -> {
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                promise.complete(errmsg);
                Logger.logger.info("response from sendNews: " + ar.result().bodyAsString());
            } else {
                Logger.logger.error("error when sendNews: " + ar.cause().getMessage());
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }

    public Future<String> getToken() {
        Promise<String> promise = Promise.promise();
        WebClient client = WebClient.create(vertx, Session.options);
        String secret = Session.getSecret((String) vertx.sharedData().getLocalMap("args").get("node"));
        String uri = Session.router.get_token_uri + secret;
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
