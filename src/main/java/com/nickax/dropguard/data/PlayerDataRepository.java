package com.nickax.dropguard.data;

import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerDataRepository extends DualRepository<UUID, PlayerData> {

    public PlayerDataRepository(Repository<UUID, PlayerData> cache, Repository<UUID, PlayerData> storage) {
        super(cache, storage);
    }

    public void loadFromStorageToCache() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadFromStorageToCache(player);
        }
    }

    public void loadFromStorageToCache(Player player) {
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

    public void saveFromCacheToStorage() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveFromCacheToStorage(player);
        }
    }

    public void saveFromCacheToStorage(Player player) {
        UUID key = player.getUniqueId();
        PlayerData playerData = get(key, TargetRepository.ONE);
        if (playerData != null) {
            put(key, playerData, TargetRepository.TWO);
        }
    }
}
