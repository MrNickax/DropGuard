package com.nickax.dropguard.data;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.config.MainConfig;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDataSaveTask extends BukkitRunnable {

    private final DropGuard plugin;
    private final PlayerDataRepository playerDataRepository;

    public PlayerDataSaveTask(DropGuard plugin) {
        this.plugin = plugin;
        this.playerDataRepository = plugin.getPlayerDataRepository();
    }

    public void start(MainConfig mainConfig) {
        if (mainConfig.isDataAutoSaveEnabled()) {
            int interval = mainConfig.getDataAutoSaveInterval() * 60 * 20;
            runTaskTimer(plugin, interval, interval);
        }
    }

    @Override
    public void run() {
        playerDataRepository.saveFromCacheToStorage();
    }
}
