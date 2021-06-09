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
        if (plugin.getConfig().getBoolean("general-options." + ".drop-confirm-message")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".drop-confirm-message")));
        }
    }

    public void toggle(Player player) {

        if (plugin.dataManager.getEnabled().getOrDefault(player.getUniqueId(), true)) {
            String status = ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("placeholders." + ".disabled-text"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +plugin.langManager.getLang().getString("plugin-messages." + ".drop-confirm-toggle-message")).replace("%status%", status));
        } else {
            String status = ChatColor.translateAlternateColorCodes('&', "" +plugin.langManager.getLang().getString("placeholders." + ".enabled-text"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" +plugin.langManager.getLang().getString("plugin-messages." + ".drop-confirm-toggle-message")).replace("%status%", status));
        }
    }

    public void togglePlayer(CommandSender sender, Player target) {
        if (plugin.dataManager.getEnabled().getOrDefault(target.getUniqueId(), true)) {
            String status = ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("placeholders." + ".disabled-text"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".drop-confirm-toggle-message-player")).replace("%status%", status).replace("%player%", target.getName()));
        } else {
            String status = ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("placeholders." + ".enabled-text"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".drop-confirm-toggle-message-player")).replace("%status%", status).replace("%player%", target.getName()));
        }
    }

    public void playerNotFound(CommandSender sender, String target) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".player-not-found-message")).replace("%player%", target));
    }

    public void reload(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".reload-message")));
    }

    public void noPermission(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".no-permission-message")));
    }

    public void unknownCommand(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".unknown-command-message")));
    }

    public void unknownCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "" + plugin.langManager.getLang().getString("plugin-messages." + ".unknown-command-message")));
    }
}