package com.nickax.dropguard.drop.confirmation;

import com.nickax.dropguard.data.PlayerData;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfirmationTimeout extends BukkitRunnable {

    private final ConfirmationTimeoutMonitor monitor;
    private final PlayerData playerData;

    public ConfirmationTimeout(ConfirmationTimeoutMonitor monitor, PlayerData playerData) {
        this.monitor = monitor;
        this.playerData = playerData;
    }

    @Override
    public void run() {
        playerData.setLastDropAttempt(null);
        monitor.removeTimeout(playerData.getId());
    }
}
