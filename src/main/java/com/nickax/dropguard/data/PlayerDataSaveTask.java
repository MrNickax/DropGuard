package com.nickax.dropguard.data;

import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerDataSaveTask extends BukkitRunnable {

    private final DualRepository<UUID, PlayerData> dualRepository;

    public PlayerDataSaveTask(DualRepository<UUID, PlayerData> dualRepository) {
        this.dualRepository = dualRepository;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player ->
                dualRepository.put(player.getUniqueId(), dualRepository.get(player.getUniqueId(), TargetRepository.ONE), TargetRepository.TWO));
    }
}
