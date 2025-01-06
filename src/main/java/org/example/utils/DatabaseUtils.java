package org.example.utils;

import java.sql.*;

public class DatabaseUtils {

    private static final String JDBC_URL = "jdbc:h2:~/test";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public static void executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String sql, Object... params) {
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, params);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }
}
