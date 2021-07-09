package me.nickax.dropconfirm.data.storage;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.entity.Player;

public class StorageManager {

    private final DropConfirm plugin;

    public StorageManager(DropConfirm plugin) {
        this.plugin = plugin;
    }

    public void save(Player player) {
        if (plugin.getConfigManager().config().getBoolean("mysql." + ".enabled")) {
            plugin.getMysqlStorage().save(player);
        } else {
            plugin.getYamlStorage().save(player);
        }
    }

    public void restore(Player player) {
        if (plugin.getConfigManager().config().getBoolean("mysql." + ".enabled")) {
            plugin.getMysqlStorage().restore(player);
        } else {
            plugin.getYamlStorage().restore(player);
        }
    }
}
