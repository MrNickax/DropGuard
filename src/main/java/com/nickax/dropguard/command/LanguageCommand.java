package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.language.LanguageManager;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.BaseCommandConfig;
import com.nickax.genten.data.DataCoordinator;
import com.nickax.genten.data.DataSource;
import com.nickax.genten.language.Language;
import com.nickax.genten.util.string.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LanguageCommand extends BaseCommand {

    private final LanguageManager languageManager;
    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public LanguageCommand(DropGuard plugin, BaseCommand parent) {
        super("language", createConfig(parent), parent.getMessages());
        this.languageManager = plugin.getLanguageManager();
        this.dataCoordinator = plugin.getDataCoordinator();
    }

    @Override
    public boolean run(CommandSender sender, String name, String[] args) {
        if (args.length < 1) {
            return usage(sender);
        }

        Language language = languageManager.getLanguage(args[0]);
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
        languageManager.sendMessage("usage", sender, new Replacement("{command_name}", getFullName()), new Replacement("{command_syntax}", getSyntax()));
        return true;
    }

    private boolean unknownLanguage(CommandSender sender) {
        languageManager.sendMessage("unknown-language", sender);
        return true;
    }

    private boolean invalidCommandSender(CommandSender sender) {
        languageManager.sendMessage("invalid-command-sender", sender);
        return true;
    }

    private boolean unknownPlayer(CommandSender sender) {
        languageManager.sendMessage("unknown-player", sender);
        return true;
    }

    private boolean noPermission(CommandSender sender) {
        languageManager.sendMessage("no-permission", sender);
        return true;
    }

    private boolean updatePlayerData(Player target, CommandSender sender, Language language) {
        PlayerData playerData = dataCoordinator.get(target.getUniqueId(), DataSource.CACHE);
        playerData.setLanguage(language.getId());
        languageChanged(target, sender, language);
        return true;
    }

    private void languageChanged(Player target, CommandSender sender, Language language) {
        if (sender.equals(target)) {
            languageManager.sendMessage("language-updated", sender, new Replacement("{language}", language.getId()));
        } else {
            languageManager.sendMessage("language-updated-other", sender, new Replacement("{language}", language.getId()), new Replacement("{player_name}", target.getName()));
            languageManager.sendMessage("language-updated", target, new Replacement("{language}", language.getId()));
        }
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String name, String[] args) {
        switch (args.length) {
            case 1:
                return languageManager.getLanguages().stream().map(Language::getId).collect(Collectors.toList());
            case 2:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    private static BaseCommandConfig createConfig(BaseCommand parent) {
        return BaseCommandConfig.newBuilder().withSyntax("[language] <player>").withParent(parent).withPermission("dropguard.language")
                .withDescription("Change the language for yourself or for another player").build();
    }
}
