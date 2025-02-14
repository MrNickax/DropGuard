package com.nickax.dropguard;

import com.nickax.dropguard.command.DropGuardCommand;
import com.nickax.dropguard.config.MainConfig;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.data.PlayerDataRepository;
import com.nickax.dropguard.data.PlayerDataSaveTask;
import com.nickax.dropguard.drop.DropEvaluator;
import com.nickax.dropguard.drop.confirmation.ConfirmationTimeoutMonitor;
import com.nickax.dropguard.listener.LanguageListener;
import com.nickax.dropguard.listener.PlayerDataListener;
import com.nickax.dropguard.listener.pickup.LegacyPickupListener;
import com.nickax.dropguard.listener.pickup.PickupListener;
import com.nickax.dropguard.listener.DropListener;
import com.nickax.genten.command.CommandRegistry;
import com.nickax.genten.config.Config;
import com.nickax.genten.item.Item;
import com.nickax.genten.item.loader.ItemLoader;
import com.nickax.genten.language.Language;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.language.LanguageLoader;
import com.nickax.genten.library.Libraries;
import com.nickax.genten.library.LibraryLoader;
import com.nickax.genten.listener.ListenerRegistry;
import com.nickax.genten.plugin.PluginUpdater;
import com.nickax.genten.repository.JsonRepository;
import com.nickax.genten.repository.LocalRepository;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.database.DatabaseLoader;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.spigot.SpigotVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class DropGuard extends JavaPlugin {

    private final LibraryLoader libraryLoader = new LibraryLoader(this);
    private final SpigotVersion version = SpigotVersion.getCurrent();
    private final MainConfig mainConfig = new MainConfig(this);
    private PlayerDataRepository playerDataRepository;
    private PlayerDataSaveTask playerDataSaveTask;
    private LanguageAccessor languageAccessor;
    private DropEvaluator dropEvaluator;
    private ConfirmationTimeoutMonitor confirmationTimeoutMonitor;
    // TODO CHECK IF THAT'S THE FINAL LOGIC I WANT FOR UPDATES
    private final PluginUpdater pluginUpdater = new PluginUpdater(getLogger());

    @Override
    public void onLoad() {
        libraryLoader.load(Libraries.MONGO_JAVA_DRIVER.get());
    }

    @Override
    public void onEnable() {
        SpigotVersion.checkCompatibility(getLogger());
        initializePlugin();
        pluginUpdater.checkForUpdates(this, 93135, mainConfig.isAutoUpdateEnabled());
        loadMetrics();
    }

    @Override
    public void onDisable() {
        shutdownPlugin();
    }

    public void reload() {
        shutdownPlugin();
        initializePlugin();
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public LanguageAccessor getLanguageAccessor() {
        return languageAccessor;
    }

    public PlayerDataRepository getPlayerDataRepository() {
        return playerDataRepository;
    }

    public DropEvaluator getDropEvaluator() {
        return dropEvaluator;
    }

    public ConfirmationTimeoutMonitor getConfirmationTimeoutMonitor() {
        return confirmationTimeoutMonitor;
    }

    private void initializePlugin() {
        loadMainConfig();
        loadPlayerData();
        loadLanguages();
        loadDropLogic();
        loadCommands();
    }

    private void shutdownPlugin() {
        playerDataSaveTask.cancel();
        playerDataRepository.saveFromCacheToDatabase();
        CommandRegistry.unregisterAll();
        ListenerRegistry.unregisterAll();
    }

    private void loadMainConfig() {
        mainConfig.load();
        mainConfig.save();
        mainConfig.restore();
    }

    private void loadPlayerData() {
        loadPlayerDataRepository();
        startPlayerDataSaveTask();
        ListenerRegistry.register(new PlayerDataListener(this));
    }

    private void loadPlayerDataRepository() {
        Repository<UUID, PlayerData> database = DatabaseLoader.load(mainConfig, PlayerData.class, getDefaultDataBase());
        playerDataRepository = new PlayerDataRepository(new LocalRepository<>(), database);
        playerDataRepository.loadFromDatabaseToCache();
    }

    private Repository<UUID, PlayerData> getDefaultDataBase() {
        return new JsonRepository<>(new File(getDataFolder(), "data/player"), PlayerData.class);
    }

    private void startPlayerDataSaveTask() {
        playerDataSaveTask = new PlayerDataSaveTask(this);
        playerDataSaveTask.start(mainConfig);
    }

    private void loadLanguages() {
        loadLanguageAccessor();
        ListenerRegistry.register(new LanguageListener(this));
    }

    private void loadLanguageAccessor() {
        List<Language> languages = getLanguages();
        Language defaultLanguage = getDefaultLanguage(languages);
        Repository<UUID, PlayerData> cache = playerDataRepository.get(TargetRepository.ONE);
        languageAccessor = new LanguageAccessor(languages, defaultLanguage, cache);
    }

    private List<Language> getLanguages() {
        LanguageLoader languageLoader = new LanguageLoader(this);
        return languageLoader.load(mainConfig.getEnabledLanguages());
    }

    // TODO CLEAN METHOD
    private Language getDefaultLanguage(List<Language> languages) {
        return languages.stream()
                .filter(language -> language.getId().equals(mainConfig.getDefaultLanguage()))
                .findFirst()
                .orElseGet(() -> {
                    getLogger().warning(String.format("Default language '%s' not found. Using first language.", mainConfig.getDefaultLanguage()));
                    return languages.get(0);
                });
    }

    private void loadDropLogic() {
        List<Item> protectedItems = loadProtectedItems();
        dropEvaluator = new DropEvaluator(protectedItems, playerDataRepository.get(TargetRepository.ONE));
        confirmationTimeoutMonitor = new ConfirmationTimeoutMonitor(this);
        registerDropAndPickupListeners();
    }

    private List<Item> loadProtectedItems() {
        Config itemConfig = loadItemConfig();
        ItemLoader itemLoader = new ItemLoader(getLogger());
        return itemLoader.load(itemConfig);
    }

    private Config loadItemConfig() {
        Config itemConfig = new Config(this, "protected_items.yml", "protected_items.yml");
        itemConfig.load();
        itemConfig.save();
        return itemConfig;
    }
    
    private void registerDropAndPickupListeners() {
        ListenerRegistry.register(new DropListener(this));
        registerPickupListener();
    }

    private void registerPickupListener() {
        if (version.isAtLeast(SpigotVersion.V1_13)) {
            ListenerRegistry.register(new PickupListener(this));
        } else {
            ListenerRegistry.register(new LegacyPickupListener(this));
        }
    }

    private void loadCommands() {
        DropGuardCommand dropGuardCommand = new DropGuardCommand(this);
        CommandRegistry.register(dropGuardCommand);
    }

    private void loadMetrics() {
        new Metrics(this, 24689);
    }
}
