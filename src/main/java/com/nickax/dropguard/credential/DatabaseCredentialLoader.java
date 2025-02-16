package com.nickax.dropguard.credential;

import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.credential.MongoCredential;
import com.nickax.genten.credential.MySQLCredential;
import org.bukkit.configuration.ConfigurationSection;

public class DatabaseCredentialLoader {

    private static final String DEFAULT_MONGO_PORT = "27017";
    private static final String DEFAULT_MYSQL_PORT = "3306";
    private static final String DEFAULT_COLLECTION = "dropguard";

    public static DatabaseCredential load(ConfigurationSection databaseSection) {
        String type = databaseSection.getString("type");

        String[] address = parseAddress(type, databaseSection.getString("address"));
        String database = databaseSection.getString("database");
        String username = databaseSection.getString("username");
        String password = databaseSection.getString("password");

        return createCredential(type, address[0], address[1], database, username, password);
    }

    private static String[] parseAddress(String type, String address) {
        String[] parts = address.split(":");
        String host = parts[0];
        String port = (parts.length > 1) ? parts[1] : getDefaultPort(type);
        return new String[]{host, port};
    }

    private static String getDefaultPort(String type) {
        return type.equalsIgnoreCase("MONGODB") ? DEFAULT_MONGO_PORT : DEFAULT_MYSQL_PORT;
    }

    private static DatabaseCredential createCredential(String type, String host, String port, String database, String username, String password) {
        switch (type) {
            case "MONGODB":
                return new MongoCredential(host, port, database, DEFAULT_COLLECTION, username, password);
            case "MYSQL":
                return new MySQLCredential(host, port, database, DEFAULT_COLLECTION, username, password);
            default:
                return null;
        }
    }
}
