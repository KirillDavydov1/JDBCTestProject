package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    public static Connection getConnection() {
        Properties databaseProperties = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("src/database.properties"))) {
            databaseProperties.load(in);
            Connection connection = DriverManager.getConnection(databaseProperties.getProperty("url"),
                    databaseProperties.getProperty("username"), databaseProperties.getProperty("password"));
            connection.setAutoCommit(false);
            return connection;
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
