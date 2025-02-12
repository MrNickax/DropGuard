package com.nickax.dropguard.repository;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.credential.MongoCredential;
import com.nickax.genten.credential.MySQLCredential;
import com.nickax.genten.repository.JsonRepository;
import com.nickax.genten.repository.MongoRepository;
import com.nickax.genten.repository.MySQLRepository;
import com.nickax.genten.repository.Repository;

import java.io.File;
import java.util.UUID;

public class RepositoryFactory {

    public static Repository<UUID, PlayerData> build(String type, DropGuard plugin) {
        switch (type) {
            case "MYSQL":
                return createMySQLRepository(plugin);
            case "MONGODB":
                return createMongoRepository(plugin);
            default:
                return createJsonRepository(plugin);
        }
    }

    private static MySQLRepository<UUID, PlayerData> createMySQLRepository(DropGuard plugin) {
        DatabaseCredential databaseCredential = plugin.getMainConfig().getDatabaseCredential();
        return new MySQLRepository<>((MySQLCredential) databaseCredential, PlayerData.class);
    }

    private static MongoRepository<UUID, PlayerData> createMongoRepository(DropGuard plugin) {
        DatabaseCredential databaseCredential = plugin.getMainConfig().getDatabaseCredential();
        return new MongoRepository<>((MongoCredential) databaseCredential, PlayerData.class);
    }

    private static JsonRepository<UUID, PlayerData> createJsonRepository(DropGuard plugin) {
        return new JsonRepository<>(new File(plugin.getDataFolder(), "data/player"), PlayerData.class);
    }
}
