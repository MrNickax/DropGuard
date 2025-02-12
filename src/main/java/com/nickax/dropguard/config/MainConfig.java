package com.nickax.dropguard.config;

import com.nickax.genten.config.Config;
import com.nickax.genten.credential.DatabaseCredential;
import com.nickax.genten.credential.MongoCredential;
import com.nickax.genten.credential.MySQLCredential;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MainConfig extends Config {

    public MainConfig(JavaPlugin plugin) {
        super(plugin, "config.yml", "config.yml");
    }

    public String getDefaultLanguage() {
        return getValue("language.default").asType(String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getEnabledLanguages() {
        return getValue("language.enabled").asType(List.class);
    }

    public boolean isAutoUpdateEnabled() {
        return getValue("auto-update").asType(Boolean.class);
    }

    public String getStorageType() {
        return getValue("storage.type").asType(String.class);
    }

    public DatabaseCredential getDatabaseCredential() {
        ConfigurationSection section = getValue("storage").asType(ConfigurationSection.class);

        String host = section.getString("host");
        int port = section.getInt("port");
        String database = section.getString("database");
        String username = section.getString("username");
        String password = section.getString("password");

        return getStorageType().equals("MONGODB")
                ? new MongoCredential(host, String.valueOf(port), database, "dropguard", username, password)
                : new MySQLCredential(host, String.valueOf(port), database, "dropguard", username, password);
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
