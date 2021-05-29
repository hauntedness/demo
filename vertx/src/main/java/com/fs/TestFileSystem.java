package com.fs;

import com.collections.Tuple2;
import com.utils.Regexp;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public class TestFileSystem {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        FileSystem fs = vertx.fileSystem();
        WebClient client = WebClient.create(vertx);
        WebClientSession session = WebClientSession.create(client);
        ArrayList<Tuple2<String, String>> list1 = new ArrayList<>();
        Promise<String> promise = Promise.promise();
        Future<String> future = promise.future();
        future.onComplete(
                asyncResult -> list1.stream().sorted(Comparator.comparing(Tuple2::_1)
                ).forEach(e -> {
                            if (e._2() != null && !e._2().trim().equals("")) {
                                System.out.println(e._2().substring(1, 10));
                            }
                            Path path = Paths.get("E:\\ProgramData\\workspace\\IdeaProjects\\demo\\vertx\\src\\main\\resources\\text\\大奉打更人.txt");
                            try {
                                if (!Files.exists(path)) {
                                    Files.createFile(path);
                                }
                                Files.write(path, e._2().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                )
        );
        fs.readFile("text/uris.txt", ar -> {
            if (ar.succeeded()) {
                String uris = ar.result().toString();
                String[] us = uris.split("\\n");
                int count = us.length;
                System.out.println("count:" + count);
                Stream.iterate(0, i -> i + 1).limit(us.length).forEach(
                        i -> {
                            String uri = us[i];
                            System.out.println(uri);
                            session.get("www.x23us.us", uri).ssl(false).send(
                                    res -> {
                                        if (res.succeeded()) {
                                            String body = res.result().bodyAsString("UTF-8");
                                            int bai = i/100;
                                            int shi = i / 10;
                                            int ge = i;
                                            String han = "";
                                            String title = "\n第 " + i + " 章\n";
                                            String text = Regexp.with(body)
                                                    .findFirst("<div id=\"content\" name=\"content\">.+?</div>")
                                                    .replaceAll("&nbsp;", "")
                                                    .replaceAll("<br />", "")
                                                    .replaceAll("<div id=\"content\" name=\"content\">", "")
                                                    .replaceAll("</div>", "")
                                                    .replaceAll("第.卷", "")
                                                    .replaceAll("第.章", "");
                                            Tuple2<String, String> tuple2 = new Tuple2<>(uri, title + text);
                                            list1.add(tuple2);
                                            if (list1.size() == count) {
                                                promise.complete("");
                                            }
                                        } else {
                                            System.out.println("failed:" + res.cause());
                                        }
                                    }
                            );
                        }
                );
            } else {
                System.out.println(ar.cause());
            }
        });
    }
}
