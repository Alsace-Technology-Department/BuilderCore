package work.alsace.alsacecore.Utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DataBaseManager {

    private final HikariDataSource dataSource;

    public DataBaseManager(String host, String database, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + "/" + database + "?useSSL=false");
        config.setUsername(username);
        config.setPassword(password);

        dataSource = new HikariDataSource(config);
        createTable();
    }

    private void createTable() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO player_agreement (player_uuid, agreed) VALUES (?, ?)")) {
            statement.setString(1, playerUUID.toString());
            statement.setBoolean(2, agreed);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delPlayerAgreed(UUID playerUUID, boolean agreed) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM player_agreement WHERE player_uuid = ?")) {
            statement.setString(1, playerUUID.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
