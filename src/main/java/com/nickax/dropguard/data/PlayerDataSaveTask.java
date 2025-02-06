package com.nickax.dropguard.data;

import com.nickax.genten.data.DataCoordinator;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerDataSaveTask extends BukkitRunnable {

    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public PlayerDataSaveTask(DataCoordinator<UUID, PlayerData> dataCoordinator) {
        this.dataCoordinator = dataCoordinator;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> dataCoordinator.saveToStorageFromCache(player.getUniqueId()));
    }
}
