package com.nickax.dropguard.drop;

import com.nickax.dropguard.data.PlayerData;
import org.bukkit.scheduler.BukkitRunnable;

public class DropConfirmationTask extends BukkitRunnable {

    private final PlayerData playerData;
    private final DropConfirmationTimeout timeoutManager;

    public DropConfirmationTask(PlayerData playerData, DropConfirmationTimeout timeoutManager) {
        this.playerData = playerData;
        this.timeoutManager = timeoutManager;
    }

    @Override
    public void run() {
        playerData.removeLastDropAttempt();
        timeoutManager.remove(playerData.getUniqueId());
    }
}
