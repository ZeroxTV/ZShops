package de.zeroxtv.zshops.configuration;

import de.zeroxtv.zshops.ZShops;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by ZeroxTV
 */
public class SQLLibrary {
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/Towncraft/Towncraft.db");
            ZShops.logger.info("Successfully loaded SQLite Database");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void execute(String command) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(command);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String command) {
        try {
            return connection.createStatement().executeQuery(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeArray(ArrayList<String> commands) {
        for (String command : commands) {
            execute(command);
        }
    }

    public boolean getBoolean(ResultSet resultSet, String column) throws SQLException {
        int i = resultSet.getInt(column);
        return i == 1;
    }
}
