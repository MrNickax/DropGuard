package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.BaseCommandConfig;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends BaseCommand {

    private final DropGuard plugin;

    public ReloadCommand(DropGuard plugin, BaseCommand parent) {
        super("reload", createConfig(parent), parent.getMessages());
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, String name, String[] args) {
        plugin.reload();
        plugin.getLanguageManager().sendMessage("reload", sender);
        return true;
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String name, String[] args) {
        return new ArrayList<>();
    }

    private static BaseCommandConfig createConfig(BaseCommand parent) {
        return BaseCommandConfig.newBuilder().withParent(parent).withPermission("dropguard.reload")
                .withDescription("Reload the plugin").build();
    }
}
