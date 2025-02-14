package com.nickax.dropguard.data;

import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerDataRepository extends DualRepository<UUID, PlayerData> {

    public PlayerDataRepository(Repository<UUID, PlayerData> cache, Repository<UUID, PlayerData> database) {
        super(cache, database);
    }

    public void loadFromDatabaseToCache() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadFromDatabaseToCache(player);
        }
    }

    public void loadFromDatabaseToCache(Player player) {
        UUID key = player.getUniqueId();
        PlayerData playerData = get(key, TargetRepository.TWO);
        put(
                key,
                playerData != null
                        ? playerData
                        : new PlayerData(player),
                TargetRepository.ONE
        );
    }

    public void saveFromCacheToDatabase() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveFromCacheToDatabase(player);
        }
    }

    public void saveFromCacheToDatabase(Player player) {
        UUID key = player.getUniqueId();
        PlayerData playerData = get(key, TargetRepository.ONE);
        if (playerData != null) {
            put(key, playerData, TargetRepository.TWO);
        }
    }
}
