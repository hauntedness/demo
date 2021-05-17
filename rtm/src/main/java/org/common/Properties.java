package org.common;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

    private java.util.Properties properties;

    private Properties() {
    }

    public static Properties with(String relative_path) {
        Properties wrapper = new Properties();
        try {
            wrapper.properties = new java.util.Properties();
            InputStream inputStream = Properties.class.getClassLoader().getResourceAsStream(relative_path);
            wrapper.properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wrapper;
    }

    public String get(String key) {
        return this.properties.getProperty(key);
    }
}
