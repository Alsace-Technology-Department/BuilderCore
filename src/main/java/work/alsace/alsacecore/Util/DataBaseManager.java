package work.alsace.alsacecore.Util;

import java.sql.*;
import java.util.UUID;

public class DataBaseManager {
    private Connection connection;

    public DataBaseManager(String host, String database, String username, String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS player_agreement (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "player_uuid VARCHAR(36) NOT NULL," +
                        "agreed BOOLEAN NOT NULL);")) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPlayerAgreed(UUID playerUUID) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT agreed FROM player_agreement WHERE player_uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getBoolean("agreed");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setPlayerAgreed(UUID playerUUID, boolean agreed) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO player_agreement (player_uuid, agreed) VALUES (?, ?)")) {
            statement.setString(1, playerUUID.toString());
            statement.setBoolean(2, agreed);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delPlayerAgreed(UUID playerUUID, boolean agreed) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM player_agreement WHERE player_uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
