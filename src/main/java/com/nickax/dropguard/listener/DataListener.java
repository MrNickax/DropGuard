package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.data.DataCoordinator;
import com.nickax.genten.listener.SwitchableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class DataListener extends SwitchableListener {

    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public DataListener(DropGuard plugin) {
        super(plugin);
        this.dataCoordinator = plugin.getDataCoordinator();
    }

    @EventHandler
    private void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        dataCoordinator.loadToCacheFromStorage(player.getUniqueId(), new PlayerData(player));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        dataCoordinator.saveToStorageFromCache(player.getUniqueId());
    }
}
