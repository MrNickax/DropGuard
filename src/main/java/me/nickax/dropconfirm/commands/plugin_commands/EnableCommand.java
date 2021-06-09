package me.nickax.dropconfirm.commands.plugin_commands;

import me.nickax.dropconfirm.DropConfirm;
import me.nickax.dropconfirm.commands.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnableCommand extends CommandBuilder {

    private final DropConfirm plugin = DropConfirm.getPlugin(DropConfirm.class);

    @Override
    public String command() {
        return "on";
    }

    @Override
    public String suffix() {
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
            return "/dropconfirm on" + " <player>";
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player p = (Player) sender;
                if (!p.hasPermission(permission())) {
                    plugin.messageManager.noPermission(p);
                    return;
                }
                plugin.dataManager.setEnabled(p, true);
                plugin.messageManager.toggle(p);
            } else if (args.length == 2) {
                Player p = (Player) sender;
                if (!p.hasPermission("dropconfirm.usage.others")) {
                    plugin.messageManager.noPermission(p);
                    return;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    plugin.dataManager.setEnabled(target, true);
                    plugin.messageManager.togglePlayer(sender, target);
                    plugin.messageManager.toggle(target);
                } else {
                    plugin.messageManager.playerNotFound(sender, args[1]);
                }
            }
        } else {
            if (args.length == 1) {
                Bukkit.getLogger().info(ChatColor.RED + "[DropConfirm] You can't use this command in the console!");
            } else if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target != null) {
                    plugin.dataManager.setEnabled(target, true);
                    plugin.messageManager.togglePlayer(sender, target);
                    plugin.messageManager.toggle(target);
                } else {
                    plugin.messageManager.playerNotFound(sender, args[1]);
                }
            }
        }
    }
}
