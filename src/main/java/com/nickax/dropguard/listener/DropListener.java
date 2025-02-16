package com.nickax.dropguard.listener;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.config.MainConfig;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.drop.DropEvaluator;
import com.nickax.dropguard.drop.confirmation.ConfirmationTimeoutMonitor;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.TargetRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DropListener extends SwitchableListener {

    private final DropEvaluator dropEvaluator;
    private final Repository<UUID, PlayerData> cache;
    private final LanguageAccessor languageAccessor;
    private final MainConfig mainConfig;
    private final ConfirmationTimeoutMonitor confirmationTimeoutMonitor;

    public DropListener(DropGuard plugin) {
        super(plugin);
        this.dropEvaluator = plugin.getDropEvaluator();
        this.cache = plugin.getPlayerDataRepository().get(TargetRepository.ONE);
        this.languageAccessor = plugin.getLanguageAccessor();
        this.mainConfig = plugin.getMainConfig();
        this.confirmationTimeoutMonitor = plugin.getConfirmationTimeoutMonitor();
    }

    @EventHandler
    private void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = cache.get(player.getUniqueId());
        if (playerData.isDropConfirmationEnabled()) {
            processDropEvent(event, player, event.getItemDrop().getItemStack());
        }
    }
    
    private void processDropEvent(PlayerDropItemEvent event, Player player, ItemStack item) {
        if (dropEvaluator.canDrop(player, item)) {
            confirmationTimeoutMonitor.cancelTimeout(player);
        } else {
            handleDropCancellation(event, player, item);
        }
    }

    private void handleDropCancellation(PlayerDropItemEvent event, Player player, ItemStack item) {
        updatePlayerData(player, item);
        sendConfirmationEvents(player);
        confirmationTimeoutMonitor.startTimeout(player, mainConfig.getDropConfirmationTimeOut());
        event.setCancelled(true);
    }

    private void updatePlayerData(Player player, ItemStack item) {
        PlayerData playerData = cache.get(player.getUniqueId());
        playerData.setLastDropAttempt(item);
    }

    private void sendConfirmationEvents(Player player) {
        sendConfirmationMessage(player);
        playConfirmationSound(player);
    }

    private void sendConfirmationMessage(Player player) {
        if (mainConfig.isConfirmationMessageEnabled()) {
            languageAccessor.sendMessage("drop-confirmation", player);
        }
    }

    private void playConfirmationSound(Player player) {
        if (mainConfig.isConfirmationSoundEnabled()) {
            player.playSound(player.getLocation(), mainConfig.getConfirmationSound(), 1, 1);
        }
    }
}
