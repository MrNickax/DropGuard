package com.nickax.dropguard.listener.pickup;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PickupListener extends SwitchableListener {

    private final DualRepository<UUID, PlayerData> dualRepository;

    public PickupListener(DropGuard plugin) {
        super(plugin);
        this.dualRepository = plugin.getDualRepository();
    }

    public void handlePickup(Player player, ItemStack pickup) {
        PlayerData playerData = dualRepository.get(player.getUniqueId(), TargetRepository.ONE);
        ItemStack lastDropAttempt = playerData.getLastDropAttempt();
        if (lastDropAttempt != null && lastDropAttempt.equals(pickup)) {
            playerData.removeLastDropAttempt();
        }
    }
}
