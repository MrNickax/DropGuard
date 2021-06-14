package me.nickax.dropconfirm.commands.plugin_commands;

import me.nickax.dropconfirm.DropConfirm;
import me.nickax.dropconfirm.commands.CommandBuilder;
import me.nickax.dropconfirm.commands.CommandManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends CommandBuilder {

    private final DropConfirm plugin = DropConfirm.getPlugin(DropConfirm.class);

    @Override
    public String command() {
        return "help";
    }

    @Override
    public String permission() {
        return null;
    }

    @Override
    public String description() {
        return "- shows the plugin help.";
    }

    @Override
    public String usage(CommandSender sender) {
        return "/dropconfirm help";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            CommandManager commandManager = new CommandManager();
            if (sender instanceof Player) {
                Player p = (Player) sender;
                int c = 0;
                for (CommandBuilder player_command : commandManager.getPlayerCommands()) {
                    if (player_command.permission() != null) {
                        if (p.hasPermission(player_command.permission())) {
                            c++;
                        }
                    } else {
                        c++;
                    }
                }
                if (c > 0) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8===== &6&lDropConfirm &8| &eCommand List &f1/1 &8====="));
                    p.sendMessage("");
                    p.sendMessage(ChatColor.DARK_GREEN + "   ■ =" + ChatColor.GRAY + " Optional Argument.");
                    p.sendMessage("");
                }
                for (CommandBuilder player_command : commandManager.getPlayerCommands()) {
                    if (player_command.permission() != null) {
                        if (p.hasPermission(player_command.permission())) {
                            p.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + player_command.usage(sender) + " " + ChatColor.GRAY + player_command.description());
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + player_command.usage(sender) + " " + ChatColor.GRAY + player_command.description());
                    }
                }
                if (c > 0) {
                    p.sendMessage("");
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8--- &eDisplaying &6" + c + "&e commands &8---"));
                } else {
                    plugin.getMessageManager().noPermission(p);
                }
            } else if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8===== &6&lDropConfirm &8| &eCommand List &f1/1 &8====="));
                sender.sendMessage("");
                for (CommandBuilder console_command : commandManager.getConsoleCommands()) {
                    sender.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + console_command.usage(sender) + " " + ChatColor.GRAY + console_command.description());
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8--- &eDisplaying &6" + commandManager.getConsoleCommands().size() + "&e commands &8---"));
            }
        }
    }
}
