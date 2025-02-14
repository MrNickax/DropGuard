package com.nickax.dropguard.listener.pickup;

import com.nickax.dropguard.DropGuard;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class LegacyPickupListener extends SwitchableListener {

    private final PickupHandler pickupHandler;

    public LegacyPickupListener(DropGuard plugin) {
        super(plugin);
        this.pickupHandler = new PickupHandler(plugin.getPlayerDataRepository().get(TargetRepository.ONE));
    }

    @EventHandler
    private void onPlayerPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        pickupHandler.handle(player, item);
    }
}
