package me.nickax.dropconfirm.config;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigManager {

    private final DropConfirm plugin;
    private final File file;
    private FileConfiguration fileConfiguration;

    public ConfigManager(DropConfirm plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void load() {
        Long s = System.currentTimeMillis();
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        } else if (file.exists()) {
            if (!valid()) {
                if (file.renameTo(new File(plugin.getDataFolder(), "invalid_config_" + UUID.randomUUID() + ".yml"))) {
                    plugin.saveResource("config.yml", false);
                }
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        Long e = System.currentTimeMillis();
        Bukkit.getLogger().info("[DropConfirm] Config loaded successfully in " + (e-s) + "ms!");
    }

    public void reload() {
        if (file.exists()) {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }
    }

    public FileConfiguration config() {
        return fileConfiguration;
    }

    private boolean valid() {
        AtomicBoolean valid = new AtomicBoolean(true);
        InputStream inputStream = plugin.getResource("config.yml");
        if (inputStream != null) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            configuration.getKeys(true).forEach(section -> {
                if (!section.contains("normal-items") && !section.contains("custom-items")) {
                    if (!fileConfiguration.contains(section)) {
                        valid.set(false);
                    }
                }
            });
        }
        return valid.get();
    }
}
