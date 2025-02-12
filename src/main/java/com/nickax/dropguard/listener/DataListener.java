package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class DataListener extends SwitchableListener {

    private final DualRepository<UUID, PlayerData> dualRepository;

    public DataListener(DropGuard plugin) {
        super(plugin);
        this.dualRepository = plugin.getDualRepository();
    }

    @EventHandler
    private void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = dualRepository.contains(player.getUniqueId(), TargetRepository.TWO)
                ? dualRepository.get(player.getUniqueId(), TargetRepository.TWO)
                : new PlayerData(player);
        dualRepository.put(player.getUniqueId(), playerData, TargetRepository.ONE);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        dualRepository.put(player.getUniqueId(), dualRepository.get(player.getUniqueId(), TargetRepository.ONE), TargetRepository.TWO);
    }
}
