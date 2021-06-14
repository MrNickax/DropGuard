package me.nickax.dropconfirm.lang;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class LangManager {

    private final DropConfirm plugin;
    private final String[] languages;
    private final File folder;

    public LangManager(DropConfirm plugin) {
        this.plugin = plugin;
        this.languages = new String[] {"en", "es"};
        folder = new File(plugin.getDataFolder() + "/lang");
    }

    public void load() {
        Long s = System.currentTimeMillis();
        for (String lang : languages) {
            File file = new File(plugin.getDataFolder() + "/lang/lang_" + lang + ".yml");
            if (!file.exists()) {
                plugin.saveResource("lang/lang_" + lang + ".yml", false);
            }
        }
        String lang = plugin.getConfig().getString("language");
        if (!checkExist(lang)) {
            Bukkit.getLogger().warning("[DropConfirm] Lang file: lang_" + lang + ".yml not found, the default language file (lang_en.yml) has been selected!");
        } else if (checkExist(lang)) {
            if (!checkValid("lang_" + lang)) {
                if (!checkValid("lang_en")) {
                    File file = new File(plugin.getDataFolder() + "/lang/lang_en.yml");
                    file.renameTo(new File(plugin.getDataFolder() + "/lang/invalid_" + UUID.randomUUID() + ".yml"));
                    plugin.saveResource("lang/lang_en" + ".yml", false);
                    if (!Objects.equals(plugin.getConfig().getString("language"), "en")) {
                        Bukkit.getLogger().warning("[DropConfirm] Lang file: lang_" + lang + ".yml is not a valid file, the default language file (lang_en.yml) has been selected!");
                    } else {
                        Bukkit.getLogger().warning("[DropConfirm] The default lang file: lang_en.yml is not valid, a new file has been generated!");
                    }
                } else {
                    Bukkit.getLogger().warning("[DropConfirm] Lang file: lang_" + lang + ".yml is not a valid file, the default language file (lang_en.yml) has been selected!");
                }
            }
        }
        Long e = System.currentTimeMillis();
        if (loadedLanguages() == 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + loadedLanguages() + " language successfully in " + (e-s) + "ms.");
        } else if (loadedLanguages() > 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + loadedLanguages() + " languages successfully in " + (e-s) + "ms.");
        }
    }

    public FileConfiguration getLang() {
        String lang = plugin.getConfig().getString("language");
        File file;
        if (checkValid("lang_" + lang)) {
            file = new File(plugin.getDataFolder() + "/lang/lang_" + lang + ".yml");
        } else {
            file = new File(plugin.getDataFolder() + "/lang/lang_en.yml");
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    private int loadedLanguages() {
        int v = 0;
        for (int id = 0; id < Arrays.stream(Objects.requireNonNull(folder.list())).count(); id++) {
            String lang = Arrays.toString(folder.list()).replace("[", "").replace("]", "").replace(" ", "").replace(".yml", "").split(",")[id];
            if (checkValid(lang)) {
                v++;
            }
        }
        return v;
    }

    private boolean checkValid(String lang) {
        File file = new File(plugin.getDataFolder() + "/lang/" + lang + ".yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (checkExist(lang)) {
            if (!fileConfiguration.contains("plugin-messages") && !fileConfiguration.contains("placeholders")) {
                return false;
            } else {
                return fileConfiguration.contains("plugin-messages." + ".reload-message") && fileConfiguration.contains("plugin-messages." + ".no-permission-message") && fileConfiguration.contains("plugin-messages." + ".unknown-command-message") && fileConfiguration.contains("plugin-messages." + ".player-not-found-message") && fileConfiguration.contains("plugin-messages." + ".drop-confirm-message") && fileConfiguration.contains("plugin-messages." + ".drop-confirm-toggle-message") && fileConfiguration.contains("plugin-messages." + ".drop-confirm-toggle-message-player") && fileConfiguration.contains("placeholders." + ".disabled-text") && fileConfiguration.contains("placeholders." + ".enabled-text");
            }
        } else {
            return false;
        }
    }

    private boolean checkExist(String lang) {
        if (folder.exists()) {
            if (folder.list() != null) {
                return Arrays.toString(folder.list()).contains(lang);
            }
        }
        return false;
    }
}