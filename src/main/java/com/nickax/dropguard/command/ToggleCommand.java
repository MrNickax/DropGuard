package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.dropguard.language.LanguageManager;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
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

public class ToggleCommand extends BaseCommand {

    private final LanguageManager languageManager;
    private final DualRepository<UUID, PlayerData> dualRepository;

    public ToggleCommand(DropGuard plugin, BaseCommand parent) {
        super("toggle", createProperties(parent), parent.getMessages());
        this.languageManager = plugin.getLanguageManager();
        this.dualRepository = plugin.getDualRepository();
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        Player target;
        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
        } else if (!(sender instanceof Player)) {
            return invalidCommandSender(sender);
        } else {
            target = (Player) sender;
        }

        if (target == null) {
            return unknownPlayer(sender);
        } else if (target != sender && !sender.hasPermission(getPermission() + ".other")) {
            return noPermission(sender);
        }

        return updatePlayerData(target, sender);
    }

    private boolean usage(CommandSender sender) {
        languageManager.sendMessage("usage", Message.class, sender, new StringReplacement("{command_name}", getFullName()), new StringReplacement("{command_syntax}", getSyntax()));
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

    private boolean updatePlayerData(Player target, CommandSender sender) {
        PlayerData playerData = dualRepository.get(target.getUniqueId(), TargetRepository.ONE);
        playerData.toggleDropConfirmation();
        confirmationToggle(target, sender, playerData.isDropConfirmationEnabled() ? "enabled" : "disabled");
        return true;
    }

    private void confirmationToggle(Player target, CommandSender sender, String status) {
        if (sender.equals(target)) {
            languageManager.sendMessage("drop-confirmation-toggle", Message.class, sender, new StringReplacement("{status}", status));
        } else {
            languageManager.sendMessage("drop-confirmation-toggle-other", Message.class, sender, new StringReplacement("{status}", status), new StringReplacement("{player_name}", target.getName()));
            languageManager.sendMessage("drop-confirmation-toggle", Message.class, target, new StringReplacement("{status}", status));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        switch (args.length) {
            case 1:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder().setSyntax("<player>").setParent(parent).setPermission("dropguard.toggle")
                .setDescription("Toggle dropguard for yourself or for another player").build();
    }
}
