package com.nickax.dropguard.storage;

import org.bukkit.configuration.ConfigurationSection;

public class StorageCredential {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public StorageCredential(ConfigurationSection section) {
        this(
                getHost(section.getString("address")),
                getPort(section.getString("address"), section.getString("type")),
                section.getString("database"),
                section.getString("username"),
                section.getString("password"));
    }

    public StorageCredential(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private static String getHost(String address) {
        return address.split(":")[0];
    }

    private static int getPort(String address, String type) {
        try {
            return Integer.valueOf(address.split(":")[1]);
        } catch (Exception e) {
            return type.equalsIgnoreCase("MYSQL") ? 3306 : 27017;
        }
    }
}
