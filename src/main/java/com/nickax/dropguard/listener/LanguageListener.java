package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class LanguageListener extends SwitchableListener {

    private final DualRepository<UUID, PlayerData> dataCoordinator;

    public LanguageListener(DropGuard plugin) {
        super(plugin);
        this.dataCoordinator = plugin.getDualRepository();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), TargetRepository.ONE);
        if (playerData.getLanguage() == null) {
            playerData.setLanguage(player.getLocale());
        }
    }
}
