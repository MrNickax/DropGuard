package me.nickax.dropconfirm.command.commands;

import me.nickax.dropconfirm.DropConfirm;
import me.nickax.dropconfirm.command.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload extends CommandBuilder {

    private final DropConfirm plugin = DropConfirm.getPlugin(DropConfirm.class);

    @Override
    public String command() {
        return "reload";
    }

    @Override
    public String syntax() {
        return null;
    }

    @Override
    public String permission() {
        return "- reload the config files.";
    }

    @Override
    public String description() {
        return "dropconfirm.reload";
    }

    @Override
    public String usage(CommandSender sender) {
        return "/dropconfirm reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission(permission())) {
                plugin.getMessageManager().noPermission(player);
                return;
            }
        }
        Bukkit.getLogger().info("[DropConfirm] Reloading the config...");
        plugin.getConfigManager().reload();
        plugin.getLangManager().load();
        if (plugin.getConfigManager().config().getBoolean("mysql." + ".enabled")) {
            if (plugin.getMysqlStorage().getConnection() == null) {
                plugin.getMysqlStorage().initialize();
            }
        }
        plugin.getMessageManager().reload(sender);
    }
}
