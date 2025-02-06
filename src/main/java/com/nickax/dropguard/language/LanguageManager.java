package com.nickax.dropguard.language;

import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.data.DataCoordinator;
import com.nickax.genten.data.DataSource;
import com.nickax.genten.language.Language;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.message.MessageHolder;
import com.nickax.genten.util.string.Replacement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class LanguageManager {

    private final LanguageAccessor languageAccessor;
    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public LanguageManager(LanguageAccessor languageAccessor, DataCoordinator<UUID, PlayerData> dataCoordinator) {
        this.languageAccessor = languageAccessor;
        this.dataCoordinator = dataCoordinator;
    }

    public void sendMessage(String id, CommandSender sender, Replacement... replacements) {
        Language language = getLanguage(sender);
        languageAccessor.sendMessage(id, language, sender, replacements);
    }

    public MessageHolder<List<String>> getMessageList(String id, CommandSender sender) {
        Language language = getLanguage(sender);
        return languageAccessor.getMessageList(id, language);
    }

    public MessageHolder<String> getMessage(String id, CommandSender sender) {
        Language language = getLanguage(sender);
        return languageAccessor.getMessage(id, language);
    }

    public Language getLanguage(String id) {
        return languageAccessor.getLanguage(id);
    }

    public Language getLanguage(CommandSender sender) {
        return sender instanceof Player
                ? getLanguage((Player) sender)
                : languageAccessor.getFallbackLanguage();
    }

    public Language getLanguage(Player player) {
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), DataSource.CACHE);
        return languageAccessor.getLanguage(playerData.getLanguage());
    }

    public List<Language> getLanguages() {
        return languageAccessor.getLanguages();
    }
}
