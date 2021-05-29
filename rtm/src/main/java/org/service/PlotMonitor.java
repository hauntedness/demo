package org.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.beans.PlotSlice;
import org.common.PlotCache;
import org.common.Session;
import org.utils.Logger;
import org.utils.Properties;
import org.utils.Regexp;
import org.wechat.AnotherDimention;
import org.wechat.WechatMessager;

import javax.management.relation.InvalidRelationIdException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class PlotMonitor extends AbstractVerticle {

    private final Properties properties = Properties.with("application.properties");

    @Override
    public void start() {
        vertx.setPeriodic(Long.parseLong(properties.get("messagePeriod")), event -> {
            WechatMessager messager = new WechatMessager(vertx);
            PlotCache cache = this.fetch();
            Future<String> future = messager.getToken();
            future.compose(ar -> AnotherDimention.getRandomMC(vertx, ar)).onSuccess(tuple2 -> {
                String token = tuple2._1();
                String picURL = tuple2._2();
                String title = new Date().toString();
                String desc = cache.stream().map(plot -> {
                            String msg2 = "";
                            String plotID = plot.getPlotID() + "";
                            if (plotID.length() > 20) {
                                msg2 = "**" + plotID.substring(plotID.length() - 20) + "**\n";
                            }
                            String hour = new DecimalFormat("##0")
                                    .format(plot.valuate().longValue() / 3600l);
                            String minute = new DecimalFormat("##0")
                                    .format((plot.valuate().longValue() % 3600l) / 60);
                            String msg3 = "时 间:" + hour + "小时" + minute + "分钟\n";
                            String msg4 = "进 度:" + cache.predictPercentage(plot.valuate()) + "\n";
                            String msg5 = "错 误:" + plot.getError() + "\n";
                            return msg2 + msg3 + msg4 + msg5 + "\n";
                        }
                ).reduce(String::concat).orElse("");
                if (!desc.equals("")) {
                    messager.sendNews(token, title, desc, picURL);
                }
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
                                Charset charset = Charset.forName(properties.get("charset"));
                                Optional<PlotSlice> optionalPlotSlice = Files.lines(path1, charset)
                                        .filter(line -> Regexp.with(line).find("(\\d+\\.\\d*) seconds|error|exception|fail"))
                                        .map(line -> new PlotSlice(path1.toString(), line))
                                        .reduce((p1, p2) -> {
                                                    try {
                                                        return p1.merge(p2, null);
                                                    } catch (InvalidRelationIdException e) {
                                                        e.printStackTrace();
                                                    }
                                                    return p1;
                                                }
                                        );
                                if (optionalPlotSlice.isPresent()) {
                                    optionalPlotSlice.get().setLastModified(lastModified);
                                    Session.plotCache.putIfValid(optionalPlotSlice.get());
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
