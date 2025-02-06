package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.data.DataCoordinator;
import com.nickax.genten.data.DataSource;
import com.nickax.genten.listener.SwitchableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class LanguageListener extends SwitchableListener {

    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public LanguageListener(DropGuard plugin) {
        super(plugin);
        this.dataCoordinator = plugin.getDataCoordinator();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), DataSource.CACHE);
        if (playerData.getLanguage() == null) {
            playerData.setLanguage(player.getLocale());
        }
    }
}
