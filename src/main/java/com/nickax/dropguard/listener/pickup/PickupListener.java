package com.nickax.dropguard.listener.pickup;

import com.nickax.dropguard.DropGuard;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupListener extends SwitchableListener {

    private final PickupHandler pickupHandler;

    public PickupListener(DropGuard plugin) {
        super(plugin);
        this.pickupHandler = new PickupHandler(plugin.getPlayerDataRepository().get(TargetRepository.ONE));
    }

    @EventHandler
    private void onEntityPickupItem(EntityPickupItemEvent event) {
        Player player = (Player) event.getEntity();
        ItemStack item = event.getItem().getItemStack();
        pickupHandler.handle(player, item);
    }
}
