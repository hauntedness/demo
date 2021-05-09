package org.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import org.common.Mihawk;
import org.common.Properties;
import org.common.Regexp;
import org.common.Tuple2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PlotMonitor extends AbstractVerticle {
    @Override
    public void start() throws Exception {

        vertx.setPeriodic(1000 * 60 * 60, event -> {
            // todo get system resource
            // todo check if chia is running
            Map<String, String> map = new HashMap<>();
            String node_name = Properties.with("application.properties").get("node_name");
            String plotter_log_dir = Properties.with("application.properties").get("plotter_log_dir");
            Path path = Paths.get(plotter_log_dir);
            try {
                Files.list(path).forEach(path_ -> {
                    System.out.println("INFO: " + path_);
                    try {
                        Stream<String> stream = Files.lines(path_, Charset.forName("Cp1252"))
                                .filter(line -> Regexp.with(line).find("(fail|exception|error)"));
                        stream.forEachOrdered(line -> {
                            String key = node_name + "::" + path_;
                            String value = line;
                            if (!Mihawk.cache.getOrDefault(key, "").equals("")) {
                                map.put(key, value);
                            }
                            Mihawk.cache.put(key, value);
                        });
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Future<String> future = this.getToken();
            future.onSuccess(token -> {
                System.out.println("goto send msg");
                map.forEach((k, v) ->
                        {
                            System.out.println(token);
                            System.out.println(k);
                            System.out.println(v);
                            this.sendMsg(token, k + "\n" + "   " + v);
                        }
                );
            }).onFailure(token -> {
                System.out.println("failed");
            });
        });
    }


    @Override
    public void stop() throws Exception {
    }

    private Future<Tuple2<String, String>> createChatGroup(String token) {
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
                System.out.println("create success:" + ar.result().bodyAsString());
                String chatid = ar.result().bodyAsJsonObject().getString("chatid");
                promise.complete(new Tuple2<>(chatid, token));
            } else {
                promise.fail(ar.cause());
            }
        });
        return promise.future();
    }

    private Future<Tuple2<String, String>> getChatGroup(String chatid, String token) {
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

    private Future<String> sendMsg(String chatid, String token, String text) {
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
                ar.cause().printStackTrace();
                promise.fail("failed");
            }
        });
        return promise.future();
    }

    private Future<String> sendMsg(String token, String text) {
        WebClient client = WebClient.create(vertx, Mihawk.options);
        Promise<String> promise = Promise.promise();
        HttpRequest<Buffer> request = client.post(Mihawk.router.send_msg_uri)
                .addQueryParam("access_token", token);
        JsonObject json = new JsonObject();
        json.put("touser", "songqifan002").put("msgtype", "text").put("agentid", Mihawk.AGENT_ID).put("text", new JsonObject().put("content", text));
        request.sendJsonObject(json, ar -> {
            System.out.println("sendmsg:" + token);
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                System.out.println("ar.result().:" + ar.result().bodyAsString());
                promise.complete(errmsg);
            } else {
                ar.cause().printStackTrace();
                promise.fail("failed");
            }
        });
        return promise.future();
    }

    private Future<String> getToken() {
        Promise<String> promise = Promise.promise();
        WebClient client = WebClient.create(vertx, Mihawk.options);
        String uri = Mihawk.router.get_token_uri;
        client.get(uri).send(
                ar -> {
                    if (ar.succeeded()) {
                        System.out.println(ar.result().bodyAsString());
                        JsonObject entries = ar.result().bodyAsJsonObject();
                        promise.complete(entries.getString("access_token"));
                    } else {
                        promise.fail(ar.cause());
                        ar.cause().printStackTrace();
                    }
                }
        );
        return promise.future();
    }

}
