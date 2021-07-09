package me.nickax.dropconfirm.data.listeners;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListeners implements Listener {

    private final DropConfirm plugin;

    public DataListeners(DropConfirm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.getPlayerManager().createData(player);
        if (plugin.getPlayerManager().getPlayer(player) != null) {
            plugin.getStorageManager().restore(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (plugin.getPlayerManager().getPlayer(player) != null) {
            plugin.getStorageManager().save(player);
        }
    }
}
