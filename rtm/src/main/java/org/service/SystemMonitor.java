package org.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.utils.Properties;
import org.wechat.AnotherDimention;
import org.wechat.WechatMessager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SystemMonitor extends AbstractVerticle {


    @Override
    public void start() {
        WechatMessager messager = new WechatMessager(vertx);
        vertx.setPeriodic(Long.parseLong(Properties.with("application.properties").get("messagePeriod")), event -> {
            Future<String> future = messager.getToken();
            future.compose(token -> AnotherDimention.getRandomMC(vertx, token)).onSuccess(tuple2 ->
                    exec(messager, tuple2._1(), tuple2._2())
            );
        });
    }

    private void exec(WechatMessager messager, String token, String picURL) {
        vertx.executeBlocking(promise -> {
            if (System.getProperties().getProperty("os.name").matches(".*Window.*")) {
                String user = (String) vertx.sharedData().getLocalMap("args").get("user");
                String node = (String) vertx.sharedData().getLocalMap("args").get("node");
                Arrays.stream(Properties.with("application.properties").get("shellPaths").replaceAll("\\{user\\}", user)
                        .split(",")).forEach(shellPath -> {
                            String[] cmd = {"powershell.exe", shellPath};
                            ProcessBuilder builder = new ProcessBuilder(cmd);
                            builder.redirectErrorStream(true);
                            try {
                                Process exec = builder.start();
                                BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                                String title = node + "\t" + LocalDate.now().format(DateTimeFormatter.ISO_DATE_TIME);
                                String msg = br.lines().collect(Collectors.joining("\n"));
                                messager.sendNews(token, title, msg, picURL);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        }, null);
    }

    @Override
    public void stop() {
    }


}
