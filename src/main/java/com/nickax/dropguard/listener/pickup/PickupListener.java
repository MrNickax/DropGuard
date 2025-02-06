package com.nickax.dropguard.listener.pickup;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.data.DataCoordinator;
import com.nickax.genten.data.DataSource;
import com.nickax.genten.listener.SwitchableListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PickupListener extends SwitchableListener {

    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public PickupListener(DropGuard plugin) {
        super(plugin);
        this.dataCoordinator = plugin.getDataCoordinator();
    }

    public void handlePickup(Player player, ItemStack pickup) {
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), DataSource.CACHE);
        ItemStack lastDropAttempt = playerData.getLastDropAttempt();
        if (lastDropAttempt != null && lastDropAttempt.equals(pickup)) {
            playerData.removeLastDropAttempt();
        }
    }
}
