package com.verticles;

import com.collections.Tuple2;
import com.statics.Mihawk;
import com.utils.Regexp;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlotMonitor extends AbstractVerticle {
    @Override
    public void start() throws Exception {

        String log_path = "C:/Users/Administrator/.chia/mainnet/plotter";
        Path path = Paths.get(log_path);
        Stream<String> stream = Files.list(path).map(path_ -> {
            System.out.println("INFO: " + path_);
            try {
                Stream<String> lines = Files.lines(path_, StandardCharsets.UTF_8);
                return lines.filter(
                        line -> Regexp.with(line).find("(fail|exception|error)")
                ).collect(Collectors.joining(";"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        });

        Optional<String> reduce = stream.reduce((s1, s2) -> s1 + "\n" + s2);
        byte[] bytes = reduce.orElse("").getBytes(StandardCharsets.UTF_8);

        Future<String> future = this.getToken();
        future.onSuccess(token -> {
            ByteBuffer byteBuffer = ByteBuffer.allocate(2000);
            int i = 0;
            int size = 1999;
            while (i < bytes.length) {
                byteBuffer.clear();
                if (i + size < bytes.length) {
                    byteBuffer.put(Arrays.copyOfRange(bytes, i, i + size));
                } else {
                    byteBuffer.put(Arrays.copyOfRange(bytes, i, bytes.length));
                }
                this.sendMsg(token, byteBuffer.toString());
                i += size;
            }
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
                promise.fail(ar.cause());
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
            if (ar.succeeded()) {
                String errmsg = ar.result().bodyAsJsonObject().getString("errmsg");
                promise.complete(errmsg);
            } else {
                promise.fail(ar.cause());
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
