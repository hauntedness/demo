package org.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.beans.PlotStatus;
import org.beans.PlotStatusMapper;
import org.common.PlotCache;
import org.common.Session;
import org.utils.Logger;
import org.utils.Properties;
import org.wechat.WechatMessager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class PlotMonitor extends AbstractVerticle {

    private final Properties properties = Properties.with("application.properties");

    @Override
    public void start() {
        Long[] epoch = new Long[]{1L};
        vertx.setPeriodic(Long.parseLong(properties.get("messagePeriod")), event -> {
            epoch[0] = Math.floorMod(epoch[0] + 1, 24L) + 1L;
            WechatMessager messager = new WechatMessager(vertx);
            PlotCache cache = this.fetch();
            Future<String> future = messager.getToken();
            future.onSuccess(token -> {
                cache.stream().forEach(plot -> {
                            String msg1 = properties.get("nodeName") + "\n";
                            String msg2 = "epoch: " + epoch[0] + "\n";
                            String msg3 = "";
                            String plotID = plot.getPlotID() + "";
                            if (plotID.length() > 14) {
                                msg3 = "file name: " + plotID.substring(plotID.length() - 14) + "\n";
                            }
                            String msg4 = "progress: " + plot.getProgress() + "\n";
                            String msg5 = "phase: " + plot.getPhaseName() + "\n";
                            String msg6 = "stage: " + plot.getStageValue() + "\n";
                            String msg = msg1 + msg2 + msg3 + msg4 + msg5 + msg6;
                            messager.sendMsg(token, msg);
                        }
                );
                cache.clear();
            });
        });
    }

    private PlotCache fetch() {
        String[] plotterLogPaths = properties.get("plotterLogPaths").split(",");
        Arrays.stream(plotterLogPaths).forEach(plotterLogPath -> {
            Path path = Paths.get(plotterLogPath);
            try {
                Files.list(path).filter(path1 -> !path1.toFile().isDirectory())
                        .filter(path1 ->
                                path1.toFile().lastModified() > System.currentTimeMillis() - 1000 * 60 * 60 * 2)
                        .forEach(path1 -> {
                            long lastModified = path1.toFile().lastModified();
                            try {
                                PlotStatusMapper mapper = new PlotStatusMapper();
                                Charset charset = Charset.forName(properties.get("charset"));
                                Optional<PlotStatus> max = Files.lines(path1, charset)
                                        .filter(mapper::match)
                                        .map(line -> mapper.map(path1.toString(), line))
                                        .max(Comparator.naturalOrder());
                                if (max.isPresent()) {
                                    max.get().setLastModified(lastModified);
                                    Session.plotCache.putIfValid(max.get());
                                }
                            } catch (IOException e) {
                                Logger.logger.error(e.getMessage());
                            }
                        });

            } catch (IOException ioException) {
                Logger.logger.error(ioException.getMessage());
            }
        });
        return Session.plotCache;
    }

    @Override
    public void stop() {
    }
}
