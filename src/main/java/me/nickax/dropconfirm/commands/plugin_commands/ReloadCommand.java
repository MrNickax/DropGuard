package me.nickax.dropconfirm.commands.plugin_commands;

import me.nickax.dropconfirm.DropConfirm;
import me.nickax.dropconfirm.commands.CommandBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends CommandBuilder {

    private final DropConfirm plugin = DropConfirm.getPlugin(DropConfirm.class);

    @Override
    public String command() {
        return "reload";
    }

    @Override
    public String suffix() {
        return null;
    }

    @Override
    public String permission() {
        return "dropconfirm.reload";
    }

    @Override
    public String description() {
        return "- reload the config.";
    }

    @Override
    public String usage(CommandSender sender) {
        return "/dropconfirm reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission(permission())) {
                plugin.messageManager.noPermission(p);
                return;
            }
        }
        if (args.length == 1) {
            Bukkit.getLogger().info("[DropConfirm] Reloading the config...");
            plugin.reloadConfig();
            plugin.langManager.load();
            plugin.messageManager.reload(sender);
        }
    }
}
