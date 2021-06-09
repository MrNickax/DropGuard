package me.nickax.dropconfirm.data;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private final DropConfirm plugin;
    private final Map<UUID, Boolean> enabled = new HashMap<>();

    public DataManager(DropConfirm plugin) {
        this.plugin = plugin;
    }

    public void create() {
        File file = new File(plugin.getDataFolder() + "/playerdata.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Bukkit.getLogger().warning("[DropConfirm] An error occurred creating the data file!");
            }
        }
    }

    public void save() {
        File file = new File(plugin.getDataFolder() + "/playerdata.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

        for (Map.Entry<UUID, Boolean> map : enabled.entrySet()) {
            if (map.getKey() != null && map.getValue() != null) {
                if (!fileConfiguration.contains("players." + map.getKey())) {
                    try {
                        fileConfiguration.set("players." + map.getKey() + ".enabled", map.getValue());
                        fileConfiguration.save(file);
                    } catch (Exception e ) {
                        Bukkit.getLogger().warning("[DropConfirm] An error occurred saving the data for: " + map.getKey() + "!");
                    }
                } else {
                    if (fileConfiguration.getBoolean("players." + map.getKey() + ".enabled") != map.getValue()) {
                        try {
                            fileConfiguration.set("players." + map.getKey() + ".enabled", map.getValue());
                            fileConfiguration.save(file);
                        } catch (Exception e ) {
                            Bukkit.getLogger().warning("[DropConfirm] An error occurred saving the data for: " + map.getKey() + "!");
                        }
                    }
                }
            }
        }
    }

    public void load() {
        File file = new File(plugin.getDataFolder() + "/playerdata.yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

        if (file.exists()) {
            if (fileConfiguration.getConfigurationSection("players") != null) {
                fileConfiguration.getConfigurationSection("players").getKeys(false).forEach(key -> {
                    boolean enabled = fileConfiguration.getBoolean("players." + key + ".enabled");
                    UUID uuid = UUID.fromString(key);
                    this.enabled.put(uuid, enabled);
                });
            }
        }
    }

    public FileConfiguration getData() {
        File file = new File(plugin.getDataFolder() + "/playerdata.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public void setEnabled(Player player, Boolean enabled) {
        this.enabled.put(player.getUniqueId(), enabled);
    }

    public Map<UUID, Boolean> getEnabled() {
        return enabled;
    }
}
