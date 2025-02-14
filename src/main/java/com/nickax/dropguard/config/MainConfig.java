package com.nickax.dropguard.config;

import com.google.common.reflect.TypeToken;
import com.nickax.dropguard.credential.DatabaseCredentialLoader;
import com.nickax.genten.config.Config;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.repository.database.DatabaseCredentialProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MainConfig extends Config implements DatabaseCredentialProvider {

    public MainConfig(JavaPlugin plugin) {
        super(plugin, "config.yml", "config.yml");
    }

    public boolean isAutoUpdateEnabled() {
        return getValue("auto-update").asType(Boolean.class);
    }

    public String getDefaultLanguage() {
        return getValue("language.default").asType(String.class);
    }

    public List<String> getEnabledLanguages() {
        return getValue("language.enabled").asType(new TypeToken<List<String>>() {}.getType());
    }

    public String getStorageType() {
        return getValue("storage.type").asType(String.class);
    }

    @Override
    public DatabaseCredential getDatabaseCredential() {
        ConfigurationSection databaseSection = getValue("storage").asType(ConfigurationSection.class);
        return DatabaseCredentialLoader.load(databaseSection);
    }

    public boolean isDataAutoSaveEnabled() {
        return getValue("storage.auto-save.enabled").asType(Boolean.class);
    }

    public int getDataAutoSaveInterval() {
        return getValue("storage.auto-save.interval").asType(Integer.class);
    }

    public boolean isConfirmationMessageEnabled() {
        return getValue("drop.send-confirmation-message").asType(Boolean.class);
    }

    public int getDropConfirmationTimeOut() {
        return getValue("drop.confirmation-timeout").asType(Integer.class);
    }
}
