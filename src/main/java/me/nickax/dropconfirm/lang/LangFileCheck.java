package me.nickax.dropconfirm.lang;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;

public class LangFileCheck {

    private final DropConfirm plugin;
    private final File folder;

    public LangFileCheck(DropConfirm plugin) {
        this.plugin = plugin;
        folder = new File(plugin.getDataFolder() + "/lang");
    }

    public boolean checkValid(String lang) {
        File file = new File(plugin.getDataFolder() + "/lang/" + lang + ".yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (checkExist(lang)) {
            if (!fileConfiguration.contains("plugin-messages")) {
                return false;
            } else {
                return fileConfiguration.contains("plugin-messages." + ".reload-message") && fileConfiguration.contains("plugin-messages." + ".no-permission-message") && fileConfiguration.contains("plugin-messages." + ".unknown-command-message") && fileConfiguration.contains("plugin-messages." + ".drop-confirm-message") && fileConfiguration.contains("plugin-messages." + ".drop-confirm-toggle-message");
            }
        } else {
            return false;
        }
    }

    public boolean checkExist(String lang) {
        if (folder.exists()) {
            if (folder.listFiles() != null) {
                return Arrays.toString(folder.list()).contains(lang);
            }
        }
        return false;
    }
}
