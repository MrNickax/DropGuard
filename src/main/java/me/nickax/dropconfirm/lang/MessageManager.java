package me.nickax.dropconfirm.lang;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageManager {

    private final DropConfirm plugin;

    public MessageManager(DropConfirm plugin) {
        this.plugin = plugin;
    }

    public void dropConfirm(Player player) {
        if (plugin.getConfig().getBoolean("drop." + ".confirm-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("plugin-messages." + ".drop-confirm-message")));
        }
    }

    public void toggle(Player player) {
        if (plugin.getPlayerManager().getPlayer(player).getEnabled()) {
            String status = ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("placeholders." + ".disabled-text"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +plugin.getLangManager().config().getString("plugin-messages." + ".drop-confirm-toggle-message")).replace("%status%", status));
        } else {
            String status = ChatColor.translateAlternateColorCodes('&', "" +plugin.getLangManager().config().getString("placeholders." + ".enabled-text"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +plugin.getLangManager().config().getString("plugin-messages." + ".drop-confirm-toggle-message")).replace("%status%", status));
        }
    }

    public void togglePlayer(CommandSender sender, Player target) {
        if (plugin.getPlayerManager().getPlayer(target).getEnabled()) {
            String status = ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("placeholders." + ".disabled-text"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("plugin-messages." + ".drop-confirm-toggle-message-player")).replace("%status%", status).replace("%player%", target.getName()));
        } else {
            String status = ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("placeholders." + ".enabled-text"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("plugin-messages." + ".drop-confirm-toggle-message-player")).replace("%status%", status).replace("%player%", target.getName()));
        }
    }

    public void playerNotFound(CommandSender sender, String target) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("plugin-messages." + ".player-not-found-message")).replace("%player%", target));
    }

    public void reload(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("plugin-messages." + ".reload-message")));
    }

    public void noPermission(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("plugin-messages." + ".no-permission-message")));
    }

    public void unknownCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.getLangManager().config().getString("plugin-messages." + ".unknown-command-message")));
    }
}
