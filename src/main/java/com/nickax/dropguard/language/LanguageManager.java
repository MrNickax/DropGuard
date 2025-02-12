package com.nickax.dropguard.language;

import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.language.Language;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.message.MessageHolder;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.util.string.StringReplacement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class LanguageManager {

    private final LanguageAccessor languageAccessor;
    private final DualRepository<UUID, PlayerData> dataCoordinator;

    public LanguageManager(LanguageAccessor languageAccessor, DualRepository<UUID, PlayerData> dataCoordinator) {
        this.languageAccessor = languageAccessor;
        this.dataCoordinator = dataCoordinator;
    }

    public void sendMessage(String id, Class<? extends MessageHolder<?>> type, CommandSender sender, StringReplacement... replacements) {
        Language language = getLanguage(sender);
        languageAccessor.sendMessage(id, type, language, sender, replacements);
    }

    public <V, T extends MessageHolder<V>> T getMessage(String id, Class<T> type, CommandSender sender) {
        Language language = getLanguage(sender);
        return languageAccessor.getMessage(id, type, language);
    }

    public Language getLanguage(String id) {
        return languageAccessor.getLanguage(id);
    }

    public Language getLanguage(CommandSender sender) {
        return sender instanceof Player
                ? getLanguage((Player) sender)
                : languageAccessor.getDefaultLanguage();
    }

    public Language getLanguage(Player player) {
        PlayerData playerData = dataCoordinator.get(player.getUniqueId(), TargetRepository.ONE);
        return languageAccessor.getLanguage(playerData.getLanguage());
    }

    public List<Language> getLanguages() {
        return languageAccessor.getLanguages();
    }
}
