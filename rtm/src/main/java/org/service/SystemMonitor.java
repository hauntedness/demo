package org.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.common.Properties;
import org.wechat.WechatMessager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SystemMonitor extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        WechatMessager messager = new WechatMessager(vertx);
        vertx.setPeriodic(Long.parseLong(Properties.with("application.properties").get("messagePeriod")), event -> {
            Future<String> future = messager.getToken();
            future.onSuccess(token -> exec(messager, token));
        });
    }

    private void exec(WechatMessager messager, String token) {
        vertx.executeBlocking(promise -> {
            if (System.getProperties().getProperty("os.name").matches(".*Window.*")) {
                System.out.println();
                Arrays.stream(Properties.with("application.properties").get("shellPaths")
                        .split(",")).forEach(shellPath -> {
                            String[] cmd = {"powershell.exe", shellPath};
                            ProcessBuilder builder = new ProcessBuilder(cmd);
                            builder.redirectErrorStream(true);
                            try {
                                Process exec = builder.start();
                                BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                                String msg = Properties.with("application.properties").get("nodeName") + "\n"
                                        + br.lines().collect(Collectors.joining("\n"));
                                messager.sendMsg(token, msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        }, null);
    }

    @Override
    public void stop() throws Exception {
    }


}
