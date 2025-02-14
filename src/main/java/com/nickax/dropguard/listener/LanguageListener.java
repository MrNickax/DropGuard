package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

// TODO MOVE TO GENTEN
public class LanguageListener extends SwitchableListener {

    private final Repository<UUID, PlayerData> cache;

    public LanguageListener(DropGuard plugin) {
        super(plugin);
        this.cache = plugin.getPlayerDataRepository().get(TargetRepository.ONE);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = cache.get(player.getUniqueId());
        if (playerData.getLanguageId() == null) {
            playerData.setLanguageId(player.getLocale());
        }
    }
}
