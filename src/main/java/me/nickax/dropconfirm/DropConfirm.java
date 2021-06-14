package me.nickax.dropconfirm;

import me.nickax.dropconfirm.commands.CommandAutoComplete;
import me.nickax.dropconfirm.commands.CommandManager;
import me.nickax.dropconfirm.config.ConfigManager;
import me.nickax.dropconfirm.data.DataManager;
import me.nickax.dropconfirm.dropconfirm.DropListeners;
import me.nickax.dropconfirm.lang.LangManager;
import me.nickax.dropconfirm.lang.MessageManager;
import me.nickax.dropconfirm.placeholderapi.Placeholders;
import me.nickax.dropconfirm.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public final class DropConfirm extends JavaPlugin {

    private final LangManager langManager = new LangManager(this);
    private final MessageManager messageManager = new MessageManager(this);
    private final DataManager dataManager = new DataManager(this);
    private final ConfigManager configManager = new ConfigManager(this);

    @Override
    public void onEnable() {
        // Check for dependencies:
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
            Bukkit.getLogger().info("[DropConfirm] PlaceholderAPI support enabled.");
        }
        // Load Config:
        configManager.load();
        // Register Commands:
        Long s = System.currentTimeMillis();
        registerCommands();
        CommandManager commandManager = new CommandManager();
        Long e = System.currentTimeMillis();
        if (commandManager.getPlayerCommands().size() == 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + commandManager.getPlayerCommands().size() + " command successfully in " + (e-s) + "ms.");
        } else if (commandManager.getPlayerCommands().size() > 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + commandManager.getPlayerCommands().size() + " commands successfully in " + (e-s) + "ms.");
        }
        // Load Data:
        dataManager.create();
        dataManager.load();
        // Save Data:
        if (getConfig().getBoolean("data-options." + ".auto-save-data")) {
            int timer = getConfig().getInt("data-options." + ".data-save-delay");
            new BukkitRunnable() {
                public void run() {
                    dataManager.save();
                }
            }.runTaskTimer(this, 0, timer);
        }
        // Register Events:
        registerEvents();
        // Load Lang:
        langManager.load();
        // BStats:
        int pluginId = 11680;
        new Metrics(this, pluginId);
        if (getConfig().getBoolean("check-for-updates")) {
            checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        // Save Data:
        dataManager.save();
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DropListeners(this), this);
    }

    private void registerCommands() {
        getCommand("dropconfirm").setExecutor(new CommandManager());
        getCommand("dropconfirm").setTabCompleter(new CommandAutoComplete());
    }

    private void checkForUpdates() {
        Logger logger = this.getLogger();
        new UpdateChecker(this, 93135).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("There is a new update available! Current version: " + getDescription().getVersion() + " - Latest version: " + version);
                logger.info("Download it at spigot: https://spigotmc.org/resources/93135");
            }
        });
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
