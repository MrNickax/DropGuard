package com.nickax.dropguard;

import com.nickax.dropguard.command.DropGuardCommand;
import com.nickax.dropguard.config.ItemConfig;
import com.nickax.dropguard.config.MainConfig;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.data.PlayerDataSaveTask;
import com.nickax.dropguard.drop.DropEvaluator;
import com.nickax.dropguard.item.ProtectedItem;
import com.nickax.dropguard.item.loader.ProtectedItemLoader;
import com.nickax.dropguard.language.LanguageManager;
import com.nickax.dropguard.listener.DataListener;
import com.nickax.dropguard.listener.DropListener;
import com.nickax.dropguard.listener.LanguageListener;
import com.nickax.dropguard.listener.pickup.LegacyPickupListener;
import com.nickax.dropguard.listener.pickup.StandardPickupListener;
import com.nickax.dropguard.repository.RepositoryFactory;
import com.nickax.genten.command.CommandRegistry;
import com.nickax.genten.language.Language;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.language.LanguageLoader;
import com.nickax.genten.library.Libraries;
import com.nickax.genten.library.LibraryLoader;
import com.nickax.genten.listener.SwitchableListener;
import com.nickax.genten.plugin.PluginUpdater;
import com.nickax.genten.repository.LocalRepository;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.spigot.SpigotVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class DropGuard extends JavaPlugin {

    private final SpigotVersion version = SpigotVersion.getCurrent();
    private final MainConfig mainConfig = new MainConfig(this);
    private final ItemConfig itemConfig = new ItemConfig(this);
    private LanguageManager languageManager;
    private DualRepository<UUID, PlayerData> dualRepository;
    private PlayerDataSaveTask playerDataSaveTask;
    private DropEvaluator dropEvaluator;
    private final List<SwitchableListener> listeners = new ArrayList<>();
    private final PluginUpdater pluginUpdater = new PluginUpdater(getLogger());

    @Override
    public void onLoad() {
        LibraryLoader libraryLoader = new LibraryLoader(this);
        libraryLoader.load(Libraries.MONGO_JAVA_DRIVER.get());
    }

    @Override
    public void onEnable() {
        checkCompatibility();
        initConfigs();
        initData();
        initLanguages();
        initDropEvaluator();
        initListeners();
        initCommands();
        checkForUpdates();
        initMetrics();
    }

    @Override
    public void onDisable() {
        savePlayerData();
        unregisterListeners();
        CommandRegistry.unregisterAll();
    }

    public void reload() {
        // Reload configs:
        mainConfig.reload();
        itemConfig.reload();

        // Reload Data
        playerDataSaveTask.cancel();
        savePlayerData();
        initData();

        // Reload Languages:
        initLanguages();

        initDropEvaluator();

        // Reload Listeners:
        unregisterListeners();
        initListeners();

        // Reload Commands:
        CommandRegistry.unregisterAll();
        initCommands();
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public ItemConfig getItemConfig() {
        return itemConfig;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public DualRepository<UUID, PlayerData> getDualRepository() {
        return dualRepository;
    }

    public DropEvaluator getDropEvaluator() {
        return dropEvaluator;
    }

    private void checkCompatibility() {
        if (version.equals(SpigotVersion.UNKNOWN)) {
            Bukkit.getLogger().warning("[DropGuard] The plugin is running on an unknown version of Spigot, which may cause instability or unexpected behavior.");
            Bukkit.getLogger().warning("[DropGuard] Proceed with caution and contact the plugin developers or refer to the documentation for further assistance if necessary.");
        }
    }

    private void initConfigs() {
        initMainConfig();
        initItemConfig();
    }

    private void initMainConfig() {
        mainConfig.load();
        mainConfig.save();
        mainConfig.restore();
        mainConfig.reload();
    }

    private void initItemConfig() {
        itemConfig.load();
        itemConfig.save();
        itemConfig.reload();
    }

    private void initLanguages() {
        LanguageLoader languageLoader = new LanguageLoader(this);
        List<Language> languages = languageLoader.load(mainConfig.getEnabledLanguages());
        Language defaultLanguage = languages.stream().filter(language -> language.getId().equalsIgnoreCase(mainConfig.getDefaultLanguage())).findFirst().orElse(null);
        languageManager = new LanguageManager(new LanguageAccessor(languages, defaultLanguage), dualRepository);
    }

    private void initData() {
        initDualRepository();
        loadPlayerData();
        startPlayerDataSaveTask();
    }

    private void initDualRepository() {
        Repository<UUID, PlayerData> database = loadDatabase();
        dualRepository = new DualRepository<>(new LocalRepository<>(), database);
    }

    private Repository<UUID, PlayerData> loadDatabase() {
        String type = mainConfig.getStorageType();
        return RepositoryFactory.build(type, this);
    }

    private void loadPlayerData() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = dualRepository.contains(player.getUniqueId(), TargetRepository.TWO)
                    ? dualRepository.get(player.getUniqueId(), TargetRepository.TWO)
                    : new PlayerData(player);
            dualRepository.put(player.getUniqueId(), playerData, TargetRepository.ONE);
        }
    }

    private void startPlayerDataSaveTask() {
        playerDataSaveTask = new PlayerDataSaveTask(dualRepository);
        if (mainConfig.isDataAutoSaveEnabled()) {
            int interval = mainConfig.getDataAutoSaveInterval() * 60 * 20;
            playerDataSaveTask.runTaskTimer(this, interval, interval);
        }
    }

    private void initDropEvaluator() {
        ProtectedItemLoader loader = new ProtectedItemLoader(this);
        List<ProtectedItem> protectedItems = loader.load(itemConfig);
        dropEvaluator = new DropEvaluator(protectedItems, dualRepository);
    }

    private void initListeners() {
        listeners.add(new DataListener(this));
        listeners.add(new DropListener(this));
        listeners.add(new LanguageListener(this));
        addPickupListener();
        registerListeners();
    }

    private void initCommands() {
        DropGuardCommand command = new DropGuardCommand(this);
        CommandRegistry.register(command);
    }

    private void addPickupListener() {
        if (version.isAtLeast(SpigotVersion.V1_13)) {
            listeners.add(new StandardPickupListener(this));
        } else {
            listeners.add(new LegacyPickupListener(this));
        }
    }

    private void registerListeners() {
        listeners.forEach(SwitchableListener::enable);
    }

    private void checkForUpdates() {
        getLogger().info("Checking for plugin updates...");
        if (pluginUpdater.isUpdateAvailable(this, 93135)) {
            handlePluginUpdate();
        } else {
            getLogger().info("Your plugin is up to date. No new updates are available");
        }
    }

    private void initMetrics() {
        new Metrics(this, 24689);
    }

    private void handlePluginUpdate() {
        if (mainConfig.isAutoUpdateEnabled()) {
            performPluginUpdate();
        } else {
            notifyUserAboutManualUpdate();
        }
    }

    private void performPluginUpdate() {
        getLogger().info("Automatic updates are enabled. Downloading the latest version...");
        pluginUpdater.update(this, 93135);
    }

    private void notifyUserAboutManualUpdate() {
        getLogger().info("Automatic updates are disabled. Visit the following link to download the update:");
        getLogger().info("https://www.spigotmc.org/resources/93135");
    }

    private void savePlayerData() {
        Bukkit.getOnlinePlayers().forEach(player ->
                dualRepository.put(player.getUniqueId(), dualRepository.get(player.getUniqueId(), TargetRepository.ONE), TargetRepository.TWO));
    }

    private void unregisterListeners() {
        listeners.forEach(SwitchableListener::disable);
    }
}
