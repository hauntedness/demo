package com.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private static Properties properties = new Properties();

    public static Properties with(String relative_path) {
        try {
            properties = new Properties();
            InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream(relative_path);
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
