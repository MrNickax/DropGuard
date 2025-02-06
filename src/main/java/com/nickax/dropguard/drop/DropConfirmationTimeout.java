package com.nickax.dropguard.drop;

import com.nickax.dropguard.data.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DropConfirmationTimeout {

    private final JavaPlugin plugin;
    private final Map<UUID, DropConfirmationTask> ongoingTasks;

    public DropConfirmationTimeout(JavaPlugin plugin) {
        this.plugin = plugin;
        this.ongoingTasks = new HashMap<>();
    }

    public void start(PlayerData playerData, int delay) {
        UUID playerId = playerData.getUniqueId();
        cancel(playerId);

        DropConfirmationTask task = new DropConfirmationTask(playerData, this);
        task.runTaskLater(plugin, delay * 20L);

        ongoingTasks.put(playerId, task);
    }

    public void cancel(UUID playerId) {
        DropConfirmationTask task = ongoingTasks.get(playerId);
        if (task != null) {
            task.cancel();
            remove(playerId);
        }
    }

    public void remove(UUID playerId) {
        ongoingTasks.remove(playerId);
    }
}
