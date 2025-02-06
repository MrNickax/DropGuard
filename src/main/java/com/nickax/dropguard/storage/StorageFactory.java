package com.nickax.dropguard.storage;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.storage.JsonStorage;
import com.nickax.genten.storage.MongoStorage;
import com.nickax.genten.storage.MySQLStorage;
import com.nickax.genten.storage.Storage;

import java.io.File;
import java.util.UUID;

public class StorageFactory {

    public static Storage<UUID, PlayerData> build(String type, DropGuard plugin) {
        switch (type) {
            case "MYSQL":
                return buildMySQLStorage(plugin);
            case "MONGODB":
                return buildMongoStorage(plugin);
            default:
                return buildJsonStorage(plugin);
        }
    }

    private static MySQLStorage<UUID, PlayerData> buildMySQLStorage(DropGuard plugin) {
        StorageCredential credential = plugin.getMainConfig().getStorageCredential();
        return new MySQLStorage<>(credential.getHost(), credential.getPort(), credential.getUsername(), credential.getPassword(),
                credential.getDatabase(), "dropguard", PlayerData.class);
    }

    private static MongoStorage<UUID, PlayerData> buildMongoStorage(DropGuard plugin) {
        StorageCredential credential = plugin.getMainConfig().getStorageCredential();
        return new MongoStorage<>(credential.getHost(), credential.getPort(), credential.getUsername(), credential.getPassword(),
                credential.getDatabase(), "dropguard", PlayerData.class);
    }

    private static JsonStorage<UUID, PlayerData> buildJsonStorage(DropGuard plugin) {
       return new JsonStorage<>(PlayerData.class, new File(plugin.getDataFolder() + "/data/player"));
    }
}
