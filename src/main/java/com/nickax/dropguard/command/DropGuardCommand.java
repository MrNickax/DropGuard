package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.command.messages.DropGuardCommandMessages;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.BaseCommandConfig;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class DropGuardCommand extends BaseCommand {

    public DropGuardCommand(DropGuard plugin) {
        super("dropguard", createConfig(), new DropGuardCommandMessages(plugin));
        registerSubCommand(new ReloadCommand(plugin, this));
        registerSubCommand(new LanguageCommand(plugin, this));
        registerSubCommand(new ToggleCommand(plugin, this));
    }

    @Override
    public boolean run(CommandSender sender, String name, String[] args) {
        displayHelp(sender, getPage(args));
        return true;
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String name, String[] args) {
        return new ArrayList<>();
    }

    private static BaseCommandConfig createConfig() {
        return BaseCommandConfig.newBuilder().withAliases("dg").build();
    }

    private int getPage(String[] args) {
        try {
            return Integer.parseInt(args[0]);
        } catch (Exception e) {
            return 1;
        }
    }
}
