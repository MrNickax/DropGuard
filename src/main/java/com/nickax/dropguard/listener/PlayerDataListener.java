package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerDataRepository;
import com.nickax.genten.listener.SwitchableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener extends SwitchableListener {

    private final PlayerDataRepository playerDataRepository;

    public PlayerDataListener(DropGuard plugin) {
        super(plugin);
        this.playerDataRepository = plugin.getPlayerDataRepository();
    }

    @EventHandler
    private void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        playerDataRepository.loadFromDatabaseToCache(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerDataRepository.saveFromCacheToDatabase(player);
    }
}
