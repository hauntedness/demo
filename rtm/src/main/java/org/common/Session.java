package org.common;

import io.vertx.ext.web.client.WebClientOptions;

public class Session {
    public static final String CORP_ID = "ww60f2e556a5360f8b";
    public static final PlotCache plotCache = new PlotCache();

    public static class router {
        public static String get_token_uri = "/cgi-bin/gettoken?corpid=" + Session.CORP_ID + "&corpsecret=";
        public static String create_chat_group_uri = "/cgi-bin/appchat/create";
        public static String get_chat_group_uri = "/cgi-bin/appchat/get";
        public static String send_group_msg_uri = "/cgi-bin/appchat/send";
        public static String send_msg_uri = "/cgi-bin/message/send";
    }

    public static class agents {
        public static String agent1 = "1000003";
        public static String secret1 = "Azd9ecUee2vXT5uEMRzNrADkLfmruL5kpbVf4eqIEgc";
        public static String agent2 = "1000008";
        public static String secret2 = "62AXbehrBUyC6XAIXeyaUtLNal-OiubIBBWU6O3cRY8";
        public static String agent3 = "1000010";
        public static String secret3 = "H88LkjFGrBXD6zfXL9qWQxnBHLvkHBuYlNog3tjLGAc";
        public static String agent4 = "1000005";
        public static String secret4 = "krTfvvLanTrMNNFl9UBxkC5hlYVfvyIhKOVnqgDVFkc";
        public static String agent5 = "1000006";
        public static String secret5 = "uIlZXjuLWwHz_a2FIov7HMmYpz-YtfQExPGSanN_hE0";
        public static String agent6 = "1000002";
        public static String secret6 = "D8gcrRgSyBt772lgmmpAjcrYWbGFGir3El3cAX1FH-g";
        public static String agent7 = "1000007";
        public static String secret7 = "Wh8G7LGCYwboQK8fjoXGyjG_4VXKbfvg4RDPfZBUfxg";
        public static String agentNami = "1000009";
        public static String secretNami = "gZ2IuINYQgtwXGFaXtMStWanyXSVB8d7tQvqhPQqRh0";

    }

    public static String getAgent(String nodeName) {
        if (nodeName.equalsIgnoreCase("P001")) {
            return agents.agent1;
        } else if (nodeName.equalsIgnoreCase("P002")) {
            return agents.agent2;
        } else if (nodeName.equalsIgnoreCase("P003")) {
            return agents.agent3;
        } else if (nodeName.equalsIgnoreCase("P004")) {
            return agents.agent4;
        } else if (nodeName.equalsIgnoreCase("P005")) {
            return agents.agent5;
        } else if (nodeName.equalsIgnoreCase("P006")) {
            return agents.agent6;
        } else if (nodeName.equalsIgnoreCase("P007")) {
            return agents.agent7;
        } else if (nodeName.equalsIgnoreCase("nami")) {
            return agents.agentNami;
        }
        return null;
    }

    public static String getSecret(String nodeName) {
        if (nodeName.equalsIgnoreCase("P001")) {
            return agents.secret1;
        } else if (nodeName.equalsIgnoreCase("P002")) {
            return agents.secret2;
        } else if (nodeName.equalsIgnoreCase("P003")) {
            return agents.secret3;
        } else if (nodeName.equalsIgnoreCase("P004")) {
            return agents.secret4;
        } else if (nodeName.equalsIgnoreCase("P005")) {
            return agents.secret5;
        } else if (nodeName.equalsIgnoreCase("P006")) {
            return agents.secret6;
        } else if (nodeName.equalsIgnoreCase("P007")) {
            return agents.secret7;
        } else if (nodeName.equalsIgnoreCase("nami")) {
            return agents.secretNami;
        }
        return null;
    }

    public static final WebClientOptions options = new WebClientOptions()
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
            .setSsl(true)
            .setVerifyHost(false)
            .setTrustAll(true)
            .setDefaultHost("qyapi.weixin.qq.com")
            .setDefaultPort(443);
}
