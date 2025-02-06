package com.nickax.dropguard.listener.pickup;

import com.nickax.dropguard.DropGuard;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class StandardPickupListener extends PickupListener {

    public StandardPickupListener(DropGuard plugin) {
        super(plugin);
    }

    @EventHandler
    private void onEntityPickupItem(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            ItemStack pickup = event.getItem().getItemStack();
            handlePickup((Player) entity, pickup);
        }
    }
}
