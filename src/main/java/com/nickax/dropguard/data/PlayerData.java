package com.nickax.dropguard.data;

import com.nickax.genten.language.LanguageProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerData implements LanguageProvider {

    private final UUID id;
    private final String name;
    private String languageId;
    private boolean dropConfirmationEnabled;
    private transient ItemStack lastDropAttempt;

    public PlayerData(Player player) {
        this.id = player.getUniqueId();
        this.name = player.getName();
        this.dropConfirmationEnabled = true;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getLanguageId() {
        return languageId;
    }

    @Override
    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public void toggleDropConfirmation() {
        setDropConfirmationEnabled(!dropConfirmationEnabled);
    }

    public boolean isDropConfirmationEnabled() {
        return dropConfirmationEnabled;
    }

    public void setDropConfirmationEnabled(boolean dropConfirmationEnabled) {
        this.dropConfirmationEnabled = dropConfirmationEnabled;
    }

    public ItemStack getLastDropAttempt() {
        return lastDropAttempt;
    }

    public void setLastDropAttempt(ItemStack lastDropAttempt) {
        this.lastDropAttempt = lastDropAttempt;
    }
}
