package me.nickax.dropconfirm.drop.listeners;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupListenersLegacy implements Listener {

    private final DropConfirm plugin;

    public PickupListenersLegacy(DropConfirm plugin) {
        this.plugin = plugin;
    }

    @EventHandler @SuppressWarnings("deprecation")
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (!plugin.getConfigManager().config().getBoolean("drop." + ".confirm-per-item")) return;
        if (!plugin.getConfigManager().config().getBoolean("drop." + ".reset-on-pickup")) return;
        ItemStack itemStack = e.getItem().getItemStack();
        if (plugin.getDropManager().getPrevious(player) != null) {
            ItemStack previous = plugin.getDropManager().getPrevious(player);
            if (previous.getType().equals(itemStack.getType())) {
                if (previous.getItemMeta() != null && itemStack.getItemMeta() != null) {
                    if (previous.getItemMeta().hasDisplayName() && itemStack.getItemMeta().hasDisplayName()) {
                        if (previous.getItemMeta().getDisplayName().equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
                            plugin.getDropManager().removePrevious(player);
                        }
                    } else {
                        plugin.getDropManager().removePrevious(player);
                    }
                } else {
                    plugin.getDropManager().removePrevious(player);
                }
            }
        }
    }
}
