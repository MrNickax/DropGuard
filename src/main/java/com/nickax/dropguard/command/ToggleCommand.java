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

public class ToggleCommand extends BaseCommand {

    private final LanguageManager languageManager;
    private final DataCoordinator<UUID, PlayerData> dataCoordinator;

    public ToggleCommand(DropGuard plugin, BaseCommand parent) {
        super("toggle", createConfig(parent));
        this.languageManager = plugin.getLanguageManager();
        this.dataCoordinator = plugin.getDataCoordinator();
    }

    @Override
    public boolean run(CommandSender sender, String name, String[] args) {
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
        } else if (!sender.hasPermission(getPermission() + ".other")) {
            return noPermission(sender);
        }

        return updatePlayerData(target, sender);
    }

    private boolean usage(CommandSender sender) {
        languageManager.sendMessage("usage", sender, new Replacement("{command_name}", getFullName()), new Replacement("{command_syntax}", getSyntax()));
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

    private boolean updatePlayerData(Player target, CommandSender sender) {
        PlayerData playerData = dataCoordinator.get(target.getUniqueId(), DataSource.CACHE);
        playerData.toggleDropConfirmation();
        confirmationToggle(target, sender, playerData.isDropConfirmationEnabled() ? "enabled" : "disabled");
        return true;
    }

    private void confirmationToggle(Player target, CommandSender sender, String status) {
        if (sender.equals(target)) {
            languageManager.sendMessage("drop-confirmation-toggle", sender, new Replacement("{status}", status));
        } else {
            languageManager.sendMessage("drop-confirmation-toggle-other", sender, new Replacement("{status}", status), new Replacement("{player_name}", target.getName()));
            languageManager.sendMessage("drop-confirmation-toggle", target, new Replacement("{status}", status));
        }
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String name, String[] args) {
        switch (args.length) {
            case 1:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    private static BaseCommandConfig createConfig(BaseCommand parent) {
        return BaseCommandConfig.newBuilder().withSyntax("<player>").withParent(parent).withPermission("dropguard.toggle")
                .withDescription("Toggle dropguard for yourself or for another player").build();
    }
}
