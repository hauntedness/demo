package org.wechat;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.common.Logger;
import org.common.Mihawk;
import org.common.Properties;
import org.common.Tuple2;

public class WechatMessager {

    private Vertx vertx;

    public WechatMessager(Vertx vertx) {
        this.vertx = vertx;
    }


    public Future<Tuple2<String, String>> createChatGroup(String token) {

        WebClient client = WebClient.create(vertx, Mihawk.options);
        Promise<Tuple2<String, String>> promise = Promise.promise();
        JsonObject body = new JsonObject();
        HttpRequest<Buffer> request = client.post(Mihawk.router.create_chat_group_uri)
                .addQueryParam("access_token", token);
        body.put("name", "road_to_money");
        body.put("owner", "songqifan002");
        JsonArray userlist = new JsonArray();
        userlist.add("songqifan002").add("songqifan002");
        body.put("userlist", userlist);
        body.put("chatid", "roadtomoney");
        request.sendJsonObject(body, ar -> {
            if (ar.succeeded()) {
                Logger.logger.info("createChatGroup: " + ar.result().bodyAsString());
                String chatid = ar.result().bodyAsJsonObject().getString("chatid");
                promise.complete(new Tuple2<>(chatid, token));
            } else {
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }

    public Future<Tuple2<String, String>> getChatGroup(String chatid, String token) {
        WebClient client = WebClient.create(vertx, Mihawk.options);
        Promise<Tuple2<String, String>> promise = Promise.promise();
        HttpRequest<Buffer> request = client.get(Mihawk.router.get_chat_group_uri)
                .addQueryParam("access_token", token)
                .addQueryParam("chatid", chatid);
        request.send(ar -> {
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                if ("ok".equals(errmsg)) {
                    promise.complete(new Tuple2<>(chatid, token));
                } else {
                    promise.complete(new Tuple2<>("", token));
                }
            } else {
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }

    public Future<String> sendMsg(String chatid, String token, String text) {
        WebClient client = WebClient.create(vertx, Mihawk.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Mihawk.router.send_group_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        json.put("chatid", chatid).put("msgtype", "text").put("text", new JsonObject().put("content", text));
        request.sendJsonObject(json, ar -> {
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                promise.complete(errmsg);
            } else {
                Logger.logger.error(ar.cause().getMessage());
                promise.fail("failed");
            }
        });
        return promise.future();
    }

    public Future<String> sendMsg(String token, String text) {
        WebClient client = WebClient.create(vertx, Mihawk.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Mihawk.router.send_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        String msgTo = Properties.with("application.properties").get("msgTo");
        json.put("touser", msgTo).put("msgtype", "text").put("agentid", Mihawk.AGENT_ID).put("text", new JsonObject().put("content", text));
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
        WebClient client = WebClient.create(vertx, Mihawk.options);
        String uri = Mihawk.router.get_token_uri;
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
