package me.nickax.dropconfirm.lang;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LangManager {

    private final DropConfirm plugin;
    private final String[] languages;
    private final File folder;

    public LangManager(DropConfirm plugin) {
        this.plugin = plugin;
        languages = new String[] {"en", "es"};
        folder = new File(plugin.getDataFolder() + "/lang");
    }

    public void load() {
        Long s = System.currentTimeMillis();
        for (String lang : languages) {
            File file = new File(plugin.getDataFolder() + "/lang/lang_" + lang + ".yml");
            if (!file.exists()) {
                try {
                    plugin.saveResource("lang/lang_" + lang + ".yml", false);
                } catch (Exception e) {
                    Bukkit.getLogger().warning("[DropConfirm] An error occurred loading the language: " + lang + "!");
                }
            }
        }
        String lang = plugin.getConfigManager().config().getString("language");
        if (!exist("lang_" + lang)) {
            Bukkit.getLogger().warning("[DropConfirm] Lang file lang_" + lang + ".yml not found, the default language file (lang_en.yml) has been selected!");
        } else {
            if (!valid("lang_" + lang)) {
                if (!valid("lang_en")) {
                    File file = new File(plugin.getDataFolder() + "/lang/lang_en.yml");
                    if (file.renameTo(new File(plugin.getDataFolder() + "/lang/invalid-lang_" + UUID.randomUUID() + ".yml"))) {
                        plugin.saveResource("lang/lang_en" + ".yml", false);
                        if (!Objects.equals(plugin.getConfigManager().config().getString("language"), "en")) {
                            Bukkit.getLogger().warning("[DropConfirm] Lang file lang_" + lang + ".yml is not a valid file, the default language file (lang_en.yml) has been selected!");
                        }
                        Bukkit.getLogger().warning("[DropConfirm] The default lang file lang_en.yml is not valid, a new file has been generated!");
                    }
                } else {
                    Bukkit.getLogger().warning("[DropConfirm] Lang file lang_" + lang + ".yml is not a valid file, the default language file (lang_en.yml) has been selected!");
                }
            }
        }
        Long e = System.currentTimeMillis();
        if (loadedLanguages() == 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + loadedLanguages() + " language successfully in " + (e-s) + "ms!");
        } else if (loadedLanguages() > 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + loadedLanguages() + " languages successfully in " + (e-s) + "ms!");
        }
    }

    public FileConfiguration config() {
        String lang = plugin.getConfigManager().config().getString("language");
        File file;
        if (valid("lang_" + lang)) {
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
            if (valid(lang)) {
                v++;
            }
        }
        return v;
    }
    private boolean valid(String lang) {
        AtomicBoolean valid = new AtomicBoolean(true);
        File file = new File(plugin.getDataFolder() + "/lang/" + lang + ".yml");
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!exist(lang)) {
            valid.set(false);
        } else {
            InputStream inputStream = plugin.getResource("lang/lang_en.yml");
            if (inputStream != null) {
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
                configuration.getKeys(true).forEach(section -> {
                    if (!fileConfiguration.contains(section)) {
                        valid.set(false);
                    }
                });
            }
        }
        return valid.get();
    }

    private boolean exist(String lang) {
        if (folder.exists()) {
            if (folder.listFiles() != null) {
                return Arrays.toString(folder.list()).contains(lang);
            }
        }
        return false;
    }
}
