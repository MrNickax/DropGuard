package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
import com.nickax.genten.message.Message;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends BaseCommand {

    private final DropGuard plugin;

    public ReloadCommand(DropGuard plugin, BaseCommand parent) {
        super("reload", createProperties(parent), parent.getMessages());
        this.plugin = plugin;
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        plugin.reload();
        plugin.getLanguageManager().sendMessage("reload", Message.class, sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return new ArrayList<>();
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder().setParent(parent).setPermission("dropguard.reload")
                .setDescription("Reload the plugin").build();
    }
}
