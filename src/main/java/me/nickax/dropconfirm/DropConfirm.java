package me.nickax.dropconfirm;

import me.nickax.dropconfirm.command.CommandAutoComplete;
import me.nickax.dropconfirm.command.CommandManager;
import me.nickax.dropconfirm.config.ConfigManager;
import me.nickax.dropconfirm.data.DataManager;
import me.nickax.dropconfirm.data.PlayerManager;
import me.nickax.dropconfirm.data.listeners.DataListeners;
import me.nickax.dropconfirm.data.storage.MysqlStorage;
import me.nickax.dropconfirm.data.storage.StorageManager;
import me.nickax.dropconfirm.data.storage.YamlStorage;
import me.nickax.dropconfirm.drop.DropManager;
import me.nickax.dropconfirm.drop.listeners.DropListeners;
import me.nickax.dropconfirm.drop.listeners.PickupListeners;
import me.nickax.dropconfirm.drop.listeners.PickupListenersLegacy;
import me.nickax.dropconfirm.lang.LangManager;
import me.nickax.dropconfirm.lang.MessageManager;
import me.nickax.dropconfirm.support.PlaceholderAPI;
import me.nickax.dropconfirm.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public final class DropConfirm extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private final StorageManager storageManager = new StorageManager(this);
    private final PlayerManager playerManager = new PlayerManager();
    private final LangManager langManager = new LangManager(this);
    private final MessageManager messageManager = new MessageManager(this);
    private final DropManager dropManager = new DropManager(this);
    private final YamlStorage yamlStorage = new YamlStorage(this);
    private final MysqlStorage mysqlStorage = new MysqlStorage(this);

    @Override
    public void onEnable() {
        // Check for dependencies:
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPI(this).register();
            Bukkit.getLogger().info("[DropConfirm] PlaceholderAPI support enabled!");
        }
        // Load Config:
        configManager.load();
        // Register Commands:
        registerCommands();
        // Enable Mysql:
        if (configManager.config().getBoolean("mysql." + ".enabled")) {
            mysqlStorage.initialize();
        }
        // Create Data:
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerManager.createData(player);
        }
        // Restore Data:
        for (Player player : Bukkit.getOnlinePlayers()) {
            storageManager.restore(player);
        }
        // Auto Save:
        if (configManager.config().getBoolean("data." + "auto-save")) {
            int delay = configManager.config().getInt("data." + ".save-delay");
            new BukkitRunnable() {
                public void run() {
                    for (DataManager dataManager : playerManager.getPlayerDataMap().values()) {
                        storageManager.save(dataManager.getPlayer());
                    }
                }
            }.runTaskTimer(this, 0L, delay);
        }
        // Load Lang:
        langManager.load();
        // Register Listeners:
        registerListeners();
        // BStats:
        int pluginId = 11680;
        new Metrics(this, pluginId);
        // Check For Updates:
        if (getConfig().getBoolean("general." + ".check-for-updates")) {
            checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        // Save Data:
        for (DataManager dataManager : playerManager.getPlayerDataMap().values()) {
            storageManager.save(dataManager.getPlayer());
        }
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new DataListeners(this), this);
        pm.registerEvents(new DropListeners(this), this);
        if (getServer().getVersion().contains("1.17") || getServer().getVersion().contains("1.16") || getServer().getVersion().contains("1.15") || getServer().getVersion().contains("1.14") || getServer().getVersion().contains("1.13")) {
            pm.registerEvents(new PickupListeners(this), this);
        } else {
            pm.registerEvents(new PickupListenersLegacy(this), this);
        }
    }

    private void registerCommands() {
        Long s = System.currentTimeMillis();
        getCommand("dropconfirm").setExecutor(new CommandManager());
        getCommand("dropconfirm").setTabCompleter(new CommandAutoComplete());
        CommandManager commandManager = new CommandManager();
        Long e = System.currentTimeMillis();
        if (commandManager.getPlayerCommands().size() == 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + commandManager.getPlayerCommands().size() + " command successfully in " + (e-s) + "ms!");
        } else if (commandManager.getPlayerCommands().size() > 1) {
            Bukkit.getLogger().info("[DropConfirm] Loaded " + commandManager.getPlayerCommands().size() + " commands successfully in " + (e-s) + "ms!");
        }
    }

    private void checkForUpdates() {
        Logger logger = this.getLogger();
        new UpdateChecker(this, 93135).getVersion(version -> {
            int spigot = Integer.parseInt(version.replace(".", ""));
            int plugin = Integer.parseInt(getDescription().getVersion().replace(".", ""));
            if (spigot-plugin > 0) {
                if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    logger.info("There is a new update available! Current version: " + getDescription().getVersion() + " Last Version: " + version);
                    logger.info("Download it at spigot: https://spigotmc.org/resources/93283");
                }
            }
        });
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public LangManager getLangManager() {
        return langManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public DropManager getDropManager() {
        return dropManager;
    }

    public YamlStorage getYamlStorage() {
        return yamlStorage;
    }

    public MysqlStorage getMysqlStorage() {
        return mysqlStorage;
    }
}
