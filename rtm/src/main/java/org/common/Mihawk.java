package org.common;

import io.vertx.ext.web.client.WebClientOptions;

import java.util.HashMap;

public class Mihawk {
    public static final String SECRET = Properties.with("application.properties").get("secret");
    public static final String CORP_ID = "ww60f2e556a5360f8b";
    public static final String AGENT_ID = Properties.with("application.properties").get("agent");
    public static final HashMap<String, String> cache = new HashMap<>();
    public static final PlotCache plotCache = new PlotCache();

    public static String token;
    public static String event = "";

    public static class router {
        public static String get_token_uri = "/cgi-bin/gettoken?corpid=" + Mihawk.CORP_ID + "&corpsecret=" + Mihawk.SECRET;
        public static String create_chat_group_uri = "/cgi-bin/appchat/create";
        public static String get_chat_group_uri = "/cgi-bin/appchat/get";
        public static String send_group_msg_uri = "/cgi-bin/appchat/send";
        public static String send_msg_uri = "/cgi-bin/message/send";
    }

    public static final WebClientOptions options = new WebClientOptions()
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
            .setSsl(true)
            .setVerifyHost(false)
            .setTrustAll(true)
            .setDefaultHost("qyapi.weixin.qq.com")
            .setDefaultPort(443);

}
