package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.data.PlayerData;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
import com.nickax.genten.language.LanguageAccessor;
import com.nickax.genten.message.Message;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.repository.dual.TargetRepository;
import com.nickax.genten.util.string.StringReplacement;
import com.nickax.genten.util.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ToggleCommand extends BaseCommand {

    private final LanguageAccessor languageAccessor;
    private final Repository<UUID, PlayerData> cache;

    public ToggleCommand(DropGuard plugin, BaseCommand parent) {
        super("toggle", createProperties(parent));
        this.languageAccessor = plugin.getLanguageAccessor();
        this.cache = plugin.getPlayerDataRepository().get(TargetRepository.ONE);
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        Player target = getTarget(sender, args);
        if (target == null) {
            return true;
        }
        return canToggleConfirmation(sender, target)
                ? toggleConfirmation(sender, target)
                : notifyNoPermission(sender);
    }

    private Player getTarget(CommandSender sender, String[] args) {
        return args.length > 0
                ? findPlayerByName(sender, args[0])
                : getSenderAsPlayer(sender);
    }
    
    private Player findPlayerByName(CommandSender sender, String name) {
        Player target = Bukkit.getPlayer(name);
        return target != null
                ? target
                : unknownPlayer(sender);
    }
    
    private Player unknownPlayer(CommandSender sender) {
        languageAccessor.sendMessage("unknown-player", sender);
        return null;
    }
    
    private Player getSenderAsPlayer(CommandSender sender) {
        return sender instanceof Player
                ? (Player) sender
                : notifyInvalidCommandSender(sender);
    }
    
    private Player notifyInvalidCommandSender(CommandSender sender) {
        languageAccessor.sendMessage("invalid-command-sender", sender);
        return null;
    }

    private boolean canToggleConfirmation(CommandSender sender, Player target) {
        return target == sender || sender.hasPermission("dropguard.toggle.other");
    }
    
    private boolean toggleConfirmation(CommandSender sender, Player target) {
        PlayerData playerData = cache.get(target.getUniqueId());
        playerData.toggleDropConfirmation();
        return notifyDropConfirmationToggled(sender, target, playerData.isDropConfirmationEnabled());
    }

    private boolean notifyDropConfirmationToggled(CommandSender sender, Player target, boolean enabled) {
        languageAccessor.sendMessage("drop-confirmation-toggle", target, new StringReplacement("{status}", getStatus(target, enabled)));
        if (!sender.equals(target)) {
            languageAccessor.sendMessage("drop-confirmation-toggle-other", sender, new StringReplacement("{status}", getStatus(sender, enabled)),
                    new StringReplacement("{player_name}", target.getName()));
        }
        return true;
    }

    private String getStatus(CommandSender sender, boolean enabled) {
        String status = enabled
                ? languageAccessor.getMessage("drop-confirmation-status-enabled", Message.class, sender).getValue()
                : languageAccessor.getMessage("drop-confirmation-status-disabled", Message.class, sender).getValue();
        return StringUtil.color(status);
    }

    private boolean notifyNoPermission(CommandSender sender) {
        languageAccessor.sendMessage("no-permission", sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return args.length == 1
                ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList())
                : List.of();
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder()
                .setParent(parent)
                .setSyntax("<player>")
                .setDescription("Toggle the confirmation for yourself or for another player")
                .setPermission("dropguard.toggle")
                .build();
    }
}
