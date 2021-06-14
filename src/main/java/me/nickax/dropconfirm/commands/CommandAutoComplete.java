package me.nickax.dropconfirm.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandAutoComplete implements TabCompleter {

    private final List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;

        List<String> autoComplete = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("dropconfirm")) {
            if (args.length == 1) {
                if (p.hasPermission("dropconfirm.usage") || p.hasPermission("dropconfirm.reload") || p.hasPermission("dropconfirm.usage.others")) {
                    arguments.add("help");
                }
                if (p.hasPermission("dropconfirm.reload")) {
                    arguments.add("reload");
                }
                if (p.hasPermission("dropconfirm.usage") || p.hasPermission("dropconfirm.usage.others")) {
                    arguments.add("on"); arguments.add("off");
                }
                for (String string : arguments) {
                    if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                        autoComplete.add(string);
                    }
                }
            } else if (args.length == 2) {
                arguments.clear();
                if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("on")) {
                    if (p.hasPermission("dropconfirm.usage.others")) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            autoComplete.add(player.getName());
                        }
                    }
                    for (String string : arguments) {
                        if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
                            autoComplete.add(string);
                        }
                    }
                }
                for (String string : arguments) {
                    if (string.toLowerCase().startsWith(args[1].toLowerCase()))
                        autoComplete.add(string);
                }
            }
            return autoComplete;
        }
        return null;
    }

}
