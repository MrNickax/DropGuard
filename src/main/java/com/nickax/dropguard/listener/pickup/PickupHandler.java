package com.nickax.dropguard.listener.pickup;

import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.repository.Repository;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PickupHandler {

    private final Repository<UUID, PlayerData> cache;

    public PickupHandler(Repository<UUID, PlayerData> cache) {
        this.cache = cache;
    }

    public void handle(Player player, ItemStack pickup) {
        PlayerData playerData = cache.get(player.getUniqueId());
        ItemStack lastDropAttempt = playerData.getLastDropAttempt();
        if (lastDropAttempt != null && lastDropAttempt.isSimilar(pickup)) {
            playerData.setLastDropAttempt(null);
        }
    }
}
