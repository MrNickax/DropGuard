package com.nickax.dropguard.config;

import com.nickax.dropguard.storage.StorageCredential;
import com.nickax.genten.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MainConfig extends Config {

    public MainConfig(JavaPlugin plugin) {
        super(plugin, "config.yml", "config.yml");
    }

    public String getDefaultLanguage() {
        return get("language.default").asType(String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getEnabledLanguages() {
        return get("language.enabled").asType(List.class);
    }

    public boolean isAutoUpdateEnabled() {
        return get("auto-update").asType(Boolean.class);
    }

    public String getStorageType() {
        return get("storage.type").asType(String.class);
    }

    public StorageCredential getStorageCredential() {
        ConfigurationSection section = get("storage").asType(ConfigurationSection.class);
        return new StorageCredential(section);
    }

    public boolean isDataAutoSaveEnabled() {
        return get("storage.auto-save.enabled").asType(Boolean.class);
    }

    public int getDataAutoSaveInterval() {
        return get("storage.auto-save.interval").asType(Integer.class);
    }

    public boolean isConfirmationMessageEnabled() {
        return get("drop.send-confirmation-message").asType(Boolean.class);
    }

    public int getDropConfirmationTimeOut() {
        return get("drop.confirmation-timeout").asType(Integer.class);
    }
}
