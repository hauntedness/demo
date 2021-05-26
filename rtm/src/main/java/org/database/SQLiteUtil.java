package org.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteUtil {

    private static final String className = "org.sqlite.JDBC";

    public static Connection getConnection(String filePath) {

        Connection connection = null;
        String url = "jdbc:sqlite:" + filePath;
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(url);
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        if (connection == null) {
            throw new NullPointerException();
        }
        return connection;
    }
}
