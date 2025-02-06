package com.nickax.dropguard.data;

import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerData {

    @SerializedName("uuid")
    private final UUID uniqueId;
    @SerializedName("name")
    private final String playerName;
    private String language;
    private boolean dropConfirmationEnabled;
    private transient ItemStack lastDropAttempt;

    public PlayerData(Player player) {
        this.uniqueId = player.getUniqueId();
        this.playerName = player.getName();
        this.dropConfirmationEnabled = false;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isDropConfirmationEnabled() {
        return dropConfirmationEnabled;
    }

    public void toggleDropConfirmation() {
        this.dropConfirmationEnabled = !this.dropConfirmationEnabled;
    }

    public void setDropConfirmationEnabled(boolean dropConfirmationEnabled) {
        this.dropConfirmationEnabled = dropConfirmationEnabled;
    }

    public ItemStack getLastDropAttempt() {
        return lastDropAttempt;
    }

    public void removeLastDropAttempt() {
        setLastDropAttempt(null);
    }

    public void setLastDropAttempt(ItemStack lastDropAttempt) {
        this.lastDropAttempt = lastDropAttempt;
    }
}
