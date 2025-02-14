package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
import com.nickax.genten.language.Language;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.util.string.StringReplacement;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO MOVE TO GENTEN AS A GENERAL COMMAND AND CLEAN
public class LanguageCommand extends BaseCommand {

    private final LanguageAccessor languageAccessor;
    private final Repository<UUID, PlayerData> cache;

    public LanguageCommand(DropGuard plugin, BaseCommand parent) {
        super("language", createProperties(parent));
        this.languageAccessor = plugin.getLanguageAccessor();
        this.cache = plugin.getPlayerDataRepository().get(TargetRepository.ONE);
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        if (args.length < 1) {
            return usage(sender);
        }

        Language language = languageAccessor.getLanguage(args[0]);
        if (language == null) {
            return unknownLanguage(sender);
        }

        Player target;
        if (args.length > 1) {
            target = Bukkit.getPlayer(args[1]);
        } else if (!(sender instanceof Player)) {
            return invalidCommandSender(sender);
        } else {
            target = (Player) sender;
        }

        if (target == null) {
            return unknownPlayer(sender);
        } else if (!sender.hasPermission(getPermission() + ".other")) {
            return noPermission(sender);
        }

        return updatePlayerData(target, sender, language);
    }

    private boolean usage(CommandSender sender) {
        languageAccessor.sendMessage("usage", sender, new StringReplacement("{command_name}", getFullName()),
                new StringReplacement("{command_syntax}", getSyntax()));
        return true;
    }

    private boolean unknownLanguage(CommandSender sender) {
        languageAccessor.sendMessage("unknown-language", sender);
        return true;
    }

    private boolean invalidCommandSender(CommandSender sender) {
        languageAccessor.sendMessage("invalid-command-sender", sender);
        return true;
    }

    private boolean unknownPlayer(CommandSender sender) {
        languageAccessor.sendMessage("unknown-player", sender);
        return true;
    }

    private boolean noPermission(CommandSender sender) {
        languageAccessor.sendMessage("no-permission", sender);
        return true;
    }

    private boolean updatePlayerData(Player target, CommandSender sender, Language language) {
        PlayerData playerData = cache.get(target.getUniqueId());
        playerData.setLanguageId(language.getId());
        languageChanged(target, sender, language);
        return true;
    }

    private void languageChanged(Player target, CommandSender sender, Language language) {
        if (sender.equals(target)) {
            languageAccessor.sendMessage("language-updated", sender, new StringReplacement("{language}", language.getId()));
        } else {
            languageAccessor.sendMessage("language-updated-other", sender, new StringReplacement("{language}", language.getId()), new StringReplacement("{player_name}", target.getName()));
            languageAccessor.sendMessage("language-updated", target, new StringReplacement("{language}", language.getId()));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        switch (args.length) {
            case 1:
                return languageAccessor.getLanguages().stream().map(Language::getId).collect(Collectors.toList());
            case 2:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder()
                .setParent(parent)
                .setSyntax("[language] <player>")
                .setDescription("Change the language for yourself or for another player")
                .setPermission("dropguard.language")
                .build();
    }
}
