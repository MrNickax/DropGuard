package me.nickax.dropconfirm.config;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class ConfigManager {

    private final DropConfirm plugin;

    public ConfigManager(DropConfirm plugin) {
        this.plugin = plugin;
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
        } else if (file.exists()) {
            if (!checkValid()) {
                file.renameTo(new File(plugin.getDataFolder(), "invalid_config_" + UUID.randomUUID() + ".yml"));
                plugin.getConfig().options().copyDefaults(true);
                plugin.saveDefaultConfig();
                Bukkit.getLogger().warning("The config file: config.yml is not valid, a new file has been generated!");
            }
        }
    }

    private boolean checkValid() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (file.exists()) {
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
            if (!fileConfiguration.contains("language") || !fileConfiguration.contains("data-options") || !fileConfiguration.contains("drop-options") || !fileConfiguration.contains("item-list-enabled") || !fileConfiguration.contains("item-list")) {
                return false;
            } else {
                return fileConfiguration.contains("data-options." + ".auto-save-data") && fileConfiguration.contains("data-options." + ".data-save-delay") && fileConfiguration.contains("drop-options." + ".drop-confirm-per-item") && fileConfiguration.contains("drop-options." + ".drop-confirm-message") && fileConfiguration.contains("drop-options." + ".reset-confirm-on-pickup") && fileConfiguration.contains("drop-options." + ".drop-timer") && fileConfiguration.contains("check-for-updates");
            }
        } else {
            return false;
        }
    }
}
