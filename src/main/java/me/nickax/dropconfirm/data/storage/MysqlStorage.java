package me.nickax.dropconfirm.data.storage;

import me.nickax.dropconfirm.DropConfirm;
import me.nickax.dropconfirm.data.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class MysqlStorage {

    private final DropConfirm plugin;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private Boolean ssl;

    private Connection connection;

    public MysqlStorage(DropConfirm plugin) {
        this.plugin = plugin;
        this.host = plugin.getConfigManager().config().getString("mysql." + ".host");
        this.port = plugin.getConfigManager().config().getString("mysql." + ".port");
        this.database = plugin.getConfigManager().config().getString("mysql." + ".database");
        this.username = plugin.getConfigManager().config().getString("mysql." + ".username");
        this.password = plugin.getConfigManager().config().getString("mysql." + ".password");
        this.ssl = plugin.getConfigManager().config().getBoolean("mysql." + ".ssl");
    }

    public void initialize() {
        this.host = plugin.getConfigManager().config().getString("mysql." + ".host");
        this.port = plugin.getConfigManager().config().getString("mysql." + ".port");
        this.database = plugin.getConfigManager().config().getString("mysql." + ".database");
        this.username = plugin.getConfigManager().config().getString("mysql." + ".username");
        this.password = plugin.getConfigManager().config().getString("mysql." + ".password");
        this.ssl = plugin.getConfigManager().config().getBoolean("mysql." + ".ssl");
        try {
            connect();
            createTable();
            Bukkit.getLogger().info("[DropConfirm] Connected to the database successfully!");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("[DropConfirm] An error as occurred connecting to the database:");
            e.printStackTrace();
        }
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Bukkit.getLogger().info("[DropConfirm] Trying to connect to the database...");
        if (plugin.getConfigManager().config().getBoolean("mysql." + ".enabled")) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database + "?useSSL=" + ssl + "&autoReconnect=true", username, password);
        }
    }

    public void save(Player player) {
        if (connection != null) {
            try {
                DataManager dataManager = plugin.getPlayerManager().getPlayer(player);
                if (dataManager != null) {
                    if (dataManager.getEnabled() != null) {
                        if (!exist(player)) {
                            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO confirm_enabled(UUID, Player, Enabled) VALUES(?, ?, ?)");
                            preparedStatement.setString(1, dataManager.getPlayer().getUniqueId().toString());
                            preparedStatement.setString(2, dataManager.getPlayer().getName());
                            preparedStatement.setBoolean(3, dataManager.getEnabled());
                            preparedStatement.executeUpdate();
                        } else {
                            PreparedStatement update = connection.prepareStatement("UPDATE confirm_enabled SET Enabled=? WHERE UUID=?");
                            update.setBoolean(1, dataManager.getEnabled());
                            update.setString(2, dataManager.getPlayer().getUniqueId().toString());
                            update.executeUpdate();
                        }
                    }
                }
            } catch (Exception e) {
                Bukkit.getLogger().warning("[DropConfirm] An error as occurred saving the " + player.getName() + " data!");
            }
        }
    }

    public void restore(Player player) {
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM confirm_enabled WHERE UUID=?");
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    DataManager dataManager = plugin.getPlayerManager().getPlayer(player);
                    if (dataManager != null) {
                        boolean enabled = resultSet.getBoolean("Enabled");
                        if (dataManager.getEnabled() != enabled) {
                            dataManager.setEnabled(enabled);
                        }
                    }
                }
            } catch (SQLException e) {
                Bukkit.getLogger().warning("[DropConfirm] An error as occurred restoring the " + player.getName() + " data!");
                e.printStackTrace();
            }
        }
    }

    private boolean exist(Player player) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM confirm_enabled WHERE UUID=?");
        preparedStatement.setString(1, player.getUniqueId().toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private void createTable() throws SQLException {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS confirm_enabled"
                + "(Player varchar(20), UUID varchar(100), Enabled BOOLEAN, PRIMARY KEY(UUID))");
        preparedStatement.executeUpdate();
    }

    public Connection getConnection() {
        return connection;
    }
}
