package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.language.LanguageManager;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
import com.nickax.genten.language.Language;
import com.nickax.genten.message.Message;
import com.nickax.genten.repository.dual.DualRepository;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.util.string.StringReplacement;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LanguageCommand extends BaseCommand {

    private final LanguageManager languageManager;
    private final DualRepository<UUID, PlayerData> dualRepository;

    public LanguageCommand(DropGuard plugin, BaseCommand parent) {
        super("language", createProperties(parent), parent.getMessages());
        this.languageManager = plugin.getLanguageManager();
        this.dualRepository = plugin.getDualRepository();
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
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
        languageManager.sendMessage("usage", Message.class, sender, new StringReplacement("{command_name}", getFullName()), new StringReplacement("{command_syntax}", getSyntax()));
        return true;
    }

    private boolean unknownLanguage(CommandSender sender) {
        languageManager.sendMessage("unknown-language", Message.class, sender);
        return true;
    }

    private boolean invalidCommandSender(CommandSender sender) {
        languageManager.sendMessage("invalid-command-sender", Message.class, sender);
        return true;
    }

    private boolean unknownPlayer(CommandSender sender) {
        languageManager.sendMessage("unknown-player", Message.class, sender);
        return true;
    }

    private boolean noPermission(CommandSender sender) {
        languageManager.sendMessage("no-permission", Message.class, sender);
        return true;
    }

    private boolean updatePlayerData(Player target, CommandSender sender, Language language) {
        PlayerData playerData = dualRepository.get(target.getUniqueId(), TargetRepository.ONE);
        playerData.setLanguage(language.getId());
        languageChanged(target, sender, language);
        return true;
    }

    private void languageChanged(Player target, CommandSender sender, Language language) {
        if (sender.equals(target)) {
            languageManager.sendMessage("language-updated", Message.class, sender, new StringReplacement("{language}", language.getId()));
        } else {
            languageManager.sendMessage("language-updated-other", Message.class, sender, new StringReplacement("{language}", language.getId()), new StringReplacement("{player_name}", target.getName()));
            languageManager.sendMessage("language-updated", Message.class, target, new StringReplacement("{language}", language.getId()));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        switch (args.length) {
            case 1:
                return languageManager.getLanguages().stream().map(Language::getId).collect(Collectors.toList());
            case 2:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder().setSyntax("[language] <player>").setParent(parent).setPermission("dropguard.language")
                .setDescription("Change the language for yourself or for another player").build();
    }
}
