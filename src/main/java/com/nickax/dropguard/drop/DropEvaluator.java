package com.nickax.dropguard.drop;

import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.item.ProtectedItem;
import com.nickax.genten.data.DataCoordinator;
import com.nickax.genten.data.DataSource;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class DropEvaluator {

    private final List<ProtectedItem> protectedItems;
    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public DropEvaluator(List<ProtectedItem> protectedItems, DataCoordinator<UUID, PlayerData> dataCoordinator) {
        this.protectedItems = protectedItems;
        this.dataCoordinator = dataCoordinator;
    }

    public boolean isDropPrevented(Player player, ItemStack item) {
        ProtectedItem protectedItem = getProtectedItem(item);
        return protectedItem != null && isNewDropAttempt(player, protectedItem);
    }

    private ProtectedItem getProtectedItem(ItemStack item) {
        return protectedItems.stream().filter(protectedItem -> protectedItem.doesItemMatch(item)).findFirst().orElse(null);
    }

    private boolean isNewDropAttempt(Player player, ProtectedItem item) {
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), DataSource.CACHE);
        ItemStack lastDropAttempt = playerData.getLastDropAttempt();
        return lastDropAttempt == null || !item.doesItemMatch(lastDropAttempt);
    }
}
