package me.nickax.dropconfirm.command.commands;

import me.nickax.dropconfirm.DropConfirm;
import me.nickax.dropconfirm.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnableConfirm extends CommandBuilder {

    private final DropConfirm plugin = DropConfirm.getPlugin(DropConfirm.class);

    @Override
    public String command() {
        return "on";
    }

    @Override
    public String syntax() {
        return null;
    }

    @Override
    public String permission() {
        return "dropconfirm.usage";
    }

    @Override
    public String description() {
        return "- enable the drop confirmation.";
    }

    @Override
    public String usage(CommandSender sender) {
        if (sender instanceof Player) {
            if (sender.hasPermission("dropconfirm.usage.others")) {
                return "/dropconfirm on" + ChatColor.DARK_GREEN + " <player>";
            } else {
                return "/dropconfirm on";
            }
        } else {
            return "/dropconfirm on";
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission(permission())) {
                plugin.getMessageManager().noPermission(player);
                return;
            }
            if (args.length == 1) {
                if (plugin.getPlayerManager().getPlayer(player) != null) {
                    plugin.getPlayerManager().getPlayer(player).setEnabled(true);
                    plugin.getMessageManager().toggle(player);
                }
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (plugin.getPlayerManager().getPlayer(target) != null) {
                        if (target != player) {
                            plugin.getPlayerManager().getPlayer(target).setEnabled(true);
                            plugin.getMessageManager().togglePlayer(sender, target);
                            plugin.getMessageManager().toggle(target);
                        } else {
                            plugin.getPlayerManager().getPlayer(target).setEnabled(true);
                            plugin.getMessageManager().toggle(target);
                        }
                    }
                } else {
                    plugin.getMessageManager().playerNotFound(sender, args[1]);
                }
            }
        } else {
            if (args.length == 1) {
                Bukkit.getLogger().info(ChatColor.RED + "[DropConfirm] You can't use this command in the console!");
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    if (plugin.getPlayerManager().getPlayer(target) != null) {
                        plugin.getPlayerManager().getPlayer(target).setEnabled(true);
                        plugin.getMessageManager().togglePlayer(sender, target);
                        plugin.getMessageManager().toggle(target);
                    }
                } else {
                    plugin.getMessageManager().playerNotFound(sender, args[1]);
                }
            }
        }
    }
}
