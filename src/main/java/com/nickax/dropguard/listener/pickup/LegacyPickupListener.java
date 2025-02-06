package com.nickax.dropguard.listener.pickup;

import com.nickax.dropguard.DropGuard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class LegacyPickupListener extends PickupListener {

    public LegacyPickupListener(DropGuard plugin) {
        super(plugin);
    }

    @EventHandler
    private void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack pickup = event.getItem().getItemStack();
        handlePickup(player, pickup);
    }
}
