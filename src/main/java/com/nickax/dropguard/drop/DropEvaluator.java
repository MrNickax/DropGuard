package com.nickax.dropguard.drop;

import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.item.Item;
import com.nickax.genten.repository.Repository;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class DropEvaluator {

    private final List<Item> protectedItems;
    private final Repository<UUID, PlayerData> cache;

    public DropEvaluator(List<Item> protectedItems, Repository<UUID, PlayerData> cache) {
        this.protectedItems = protectedItems;
        this.cache = cache;
    }

    public boolean canDrop(Player player, ItemStack item) {
        boolean isUnprotectedItem = protectedItems.stream().noneMatch(protectedItem -> protectedItem.doesItemMatch(item));
        return isSameDropAttempt(player, item) || isUnprotectedItem;
    }

    private boolean isSameDropAttempt(Player player, ItemStack item) {
        PlayerData playerData = cache.get(player.getUniqueId());
        ItemStack lastDropAttempt = playerData.getLastDropAttempt();
        return lastDropAttempt != null && lastDropAttempt.isSimilar(item);
    }
}
