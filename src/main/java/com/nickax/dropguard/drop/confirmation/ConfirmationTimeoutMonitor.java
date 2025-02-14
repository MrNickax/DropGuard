package com.nickax.dropguard.drop.confirmation;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfirmationTimeoutMonitor {

    private final JavaPlugin plugin;
    private final Repository<UUID, PlayerData> cache;
    private final Map<UUID, ConfirmationTimeout> ongoingTimeouts;

    public ConfirmationTimeoutMonitor(DropGuard plugin) {
        this.plugin = plugin;
        this.cache = plugin.getPlayerDataRepository().get(TargetRepository.ONE);
        this.ongoingTimeouts = new HashMap<>();
    }

    public void startTimeout(Player player, int delayInSeconds) {
        cancelTimeout(player);

        ConfirmationTimeout timeout = createTimeoutTask(player);
        
        timeout.runTaskLater(plugin, (delayInSeconds * 20L));
        ongoingTimeouts.put(player.getUniqueId(), timeout);
    }

    public void cancelTimeout(Player player) {
        ConfirmationTimeout timeout = ongoingTimeouts.get(player.getUniqueId());
        if (timeout != null) {
            timeout.cancel();
            ongoingTimeouts.remove(player.getUniqueId());
        }
    }

    public void removeTimeout(UUID id) {
        ongoingTimeouts.remove(id);
    }

    private ConfirmationTimeout createTimeoutTask(Player player) {
        PlayerData playerData = cache.get(player.getUniqueId());
        return new ConfirmationTimeout(this, playerData);
    }
}
