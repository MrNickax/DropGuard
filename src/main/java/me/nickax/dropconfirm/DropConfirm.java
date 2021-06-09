package me.nickax.dropconfirm;

import me.nickax.dropconfirm.commands.CommandAutoComplete;
import me.nickax.dropconfirm.commands.CommandManager;
import me.nickax.dropconfirm.data.DataManager;
import me.nickax.dropconfirm.drop.DropListeners;
import me.nickax.dropconfirm.lang.LangFileCheck;
import me.nickax.dropconfirm.lang.LangManager;
import me.nickax.dropconfirm.lang.MessageManager;
import me.nickax.dropconfirm.placeholderapi.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DropConfirm extends JavaPlugin {

    public LangFileCheck langFileCheck = new LangFileCheck(this);
    public LangManager langManager = new LangManager(this);
    public MessageManager messageManager = new MessageManager(this);
    public DataManager dataManager = new DataManager(this);

    @Override
    public void onEnable() {

        // Check for dependencies:
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
            Bukkit.getLogger().info("[DropConfirm] PlaceholderAPI support enabled.");
        }
        // Load Config:
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        // Load Data:
        dataManager.create();
        if (getConfig().getBoolean("general-options." + ".save-playerdata")) {
            dataManager.load();
        }
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
        // Register Events:
        registerEvents();
        // Load Lang:
        langManager.load();
    }

    @Override
    public void onDisable() {

        // Save Data:
        if (getConfig().getBoolean("general-options." + ".save-playerdata")) {
            dataManager.save();
        }
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new DropListeners(), this);
    }

    private void registerCommands() {
        getCommand("dropconfirm").setExecutor(new CommandManager());
        getCommand("dropconfirm").setTabCompleter(new CommandAutoComplete());
    }

}
