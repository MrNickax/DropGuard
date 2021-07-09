package me.nickax.dropconfirm.data.storage;

import me.nickax.dropconfirm.DropConfirm;
import me.nickax.dropconfirm.data.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class YamlStorage {

    private final DropConfirm plugin;

    public YamlStorage(DropConfirm plugin) {
        this.plugin = plugin;
    }

    public void save(Player player) {
        File file = new File(plugin.getDataFolder() + "/playerdata", player.getUniqueId() + ".yml");
        DataManager dataManager = plugin.getPlayerManager().getPlayer(player);
        if (dataManager != null) {
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
            try {
                if (dataManager.getEnabled() != null) {
                    fileConfiguration.set("uuid", player.getUniqueId().toString());
                    fileConfiguration.set("name", player.getName());
                    fileConfiguration.set("confirm-enabled", dataManager.getEnabled());
                    fileConfiguration.options().copyDefaults();
                    fileConfiguration.save(file);
                }
            } catch (Exception e) {
                Bukkit.getLogger().warning("[DropConfirm] An error as occurred saving the " + player.getName() + " data!");
            }
        }
    }

    public void restore(Player player) {
        File file = new File(plugin.getDataFolder() + "/playerdata", player.getUniqueId() + ".yml");
        DataManager dataManager = plugin.getPlayerManager().getPlayer(player);
        if (dataManager != null) {
            if (file.exists()) {
                FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
                if (fileConfiguration.getBoolean("confirm-enabled")) {
                    dataManager.setEnabled(fileConfiguration.getBoolean("confirm-enabled"));
                }
            }
        }
    }
}
