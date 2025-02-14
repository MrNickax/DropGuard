package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    private final DropGuard plugin;

    public ReloadCommand(DropGuard plugin, BaseCommand parent) {
        super("reload", createProperties(parent));
        this.plugin = plugin;
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        plugin.reload();
        plugin.getLanguageAccessor().sendMessage("reload", sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return List.of();
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder()
                .setParent(parent)
                .setDescription("Reload the plugin")
                .setPermission("dropguard.reload")
                .build();
    }
}
