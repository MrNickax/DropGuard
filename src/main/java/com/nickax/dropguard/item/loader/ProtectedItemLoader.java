package com.nickax.dropguard.item.loader;

import com.nickax.dropguard.config.ItemConfig;
import com.nickax.dropguard.item.ProtectedItem;
import com.nickax.dropguard.item.ProtectedItemProperties;
import com.nickax.dropguard.item.validator.ProtectedItemValidator;
import com.nickax.genten.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProtectedItemLoader {

    private final Logger logger;

    public ProtectedItemLoader(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
    }

    public List<ProtectedItem> load(ItemConfig config) {
        Bukkit.getLogger().info("[DropGuard] Loading protected items...");
        List<ProtectedItem> protectedItems = proceedLoading(config);
        Bukkit.getLogger().info("[DropGuard] Loaded " + protectedItems.size() + " protected item(s)!");
        return protectedItems;
    }

    public ProtectedItem load(ConfigurationSection section) {
        ProtectedItemValidator.Result result = ProtectedItemValidator.validate(section);

        if (result.equals(ProtectedItemValidator.Result.SUCCESS)) {
            return proceedLoading(section);
        }

        printWarning(result, section.getName());
        return null;
    }

    private ProtectedItem proceedLoading(ConfigurationSection section) {
        String material = section.getString("material");
        ProtectedItemProperties properties = loadProperties(section);
        return new ProtectedItem(material, properties);
    }

    private ProtectedItemProperties loadProperties(ConfigurationSection section) {
        return new ProtectedItemProperties(getName(section), getLore(section));
    }

    private String getName(ConfigurationSection section) {
        return section.contains("properties.name") ? StringUtil.color(section.getString("properties.name")) : null;
    }

    private List<String> getLore(ConfigurationSection section) {
        return section.contains("properties.lore") ? StringUtil.color(section.getStringList("properties.lore")) : null;
    }

    private List<ProtectedItem> proceedLoading(ItemConfig config) {
        List<ProtectedItem> protectedItems = new ArrayList<>();
        config.getConfigurationSections().forEach(section -> addIfValid(section, protectedItems));
        return protectedItems;
    }

    private void addIfValid(ConfigurationSection section, List<ProtectedItem> target) {
        ProtectedItem item = load(section);
        if (item != null) {
            target.add(item);
        }
    }

    private void printWarning(ProtectedItemValidator.Result result, String itemName) {
        String warningMessage = String.format("Failed to load %s. Reason: %s", itemName, result);
        logger.warning(warningMessage);
    }
}
