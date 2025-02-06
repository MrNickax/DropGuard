package com.nickax.dropguard.config;

import com.nickax.genten.config.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ItemConfig extends Config {

    public ItemConfig(JavaPlugin plugin) {
        super(plugin, "protected_items.yml", "protected_items.yml");
    }

    public List<ConfigurationSection> getConfigurationSections() {
        List<ConfigurationSection> sections = new ArrayList<>();
        getKeys(false).forEach(key -> addIfValid(key, sections));
        return sections;
    }

    private void addIfValid(String key, List<ConfigurationSection> target) {
        ConfigurationSection section = get(key).asType(ConfigurationSection.class);
        if (section != null) {
            target.add(section);
        }
    }
}
