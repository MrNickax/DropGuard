package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.config.MainConfig;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.drop.DropConfirmationTimeout;
import com.nickax.dropguard.drop.DropEvaluator;
import com.nickax.dropguard.language.LanguageManager;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.message.Message;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DropListener extends SwitchableListener {

    private final DropEvaluator dropEvaluator;
    private final DualRepository<UUID, PlayerData> dataCoordinator;
    private final MainConfig mainConfig;
    private final LanguageManager languageManager;
    private final DropConfirmationTimeout confirmationTimeout;

    public DropListener(DropGuard plugin) {
        super(plugin);
        this.dropEvaluator = plugin.getDropEvaluator();
        this.dataCoordinator = plugin.getDualRepository();
        this.mainConfig = plugin.getMainConfig();
        this.languageManager = plugin.getLanguageManager();
        this.confirmationTimeout = new DropConfirmationTimeout(plugin);
    }

    @EventHandler
    private void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (isDropConfirmationEnabled(player)) {
            ItemStack item = event.getItemDrop().getItemStack();
            if (dropEvaluator.isDropPrevented(player, item)) {
                handleDropCancellation(event, player, item);
            } else {
                confirmationTimeout.remove(player.getUniqueId());
            }
        }
    }

    private boolean isDropConfirmationEnabled(Player player) {
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), TargetRepository.ONE);
        return playerData.isDropConfirmationEnabled();
    }

    private void handleDropCancellation(PlayerDropItemEvent event, Player player, ItemStack item) {
        updatePlayerData(player, item);
        sendConfirmationIfEnabled(player);
        event.setCancelled(true);
    }

    private void updatePlayerData(Player player, ItemStack item) {
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), TargetRepository.ONE);
        playerData.setLastDropAttempt(item);
        confirmationTimeout.start(playerData, mainConfig.getDropConfirmationTimeOut());
    }

    private void sendConfirmationIfEnabled(Player player) {
        if (mainConfig.isConfirmationMessageEnabled()) {
            languageManager.sendMessage("drop-confirmation", Message.class, player);
        }
    }
}
