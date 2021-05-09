package com.utils;

import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;

public class TestRegexp {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        FileSystem fs = vertx.fileSystem();
        fs.readFile("text/wen.txt", ar -> {
            if (ar.succeeded()) {
                String text = ar.result().toString();
                // <div id="content" name="content">
                String reg = "<div id=\"content\" name=\"content\">.+?</div>";
                // <div id="content" name="content">
                String matchString = Regexp.with(text).findFirst("<div id=\"content\" name=\"content\">.+?</div>").replaceAll("&nbsp;", "").replaceAll("<br />", "");
                System.out.println(matchString);
            }
        });
    }
}
