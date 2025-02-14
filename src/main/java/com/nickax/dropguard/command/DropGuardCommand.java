package com.nickax.dropguard.command;

import com.nickax.dropguard.DropGuard;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandHelp;
import com.nickax.genten.command.CommandProperties;
import com.nickax.genten.command.messages.MultiLanguageCommandMessages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DropGuardCommand extends BaseCommand {

    public DropGuardCommand(DropGuard plugin) {
        super("dropguard", createProperties(), createMessages(plugin));
        addSubCommand(new ReloadCommand(plugin, this));
        addSubCommand(new ToggleCommand(plugin, this));
        addSubCommand(new LanguageCommand(plugin, this));
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        CommandHelp help = getHelp(sender);
        help.display();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return List.of();
    }

    private static CommandProperties createProperties() {
        return CommandProperties.builder()
                .setAliases(List.of("dg"))
                .build();
    }

    private static MultiLanguageCommandMessages createMessages(DropGuard plugin) {
        return MultiLanguageCommandMessages.builder(plugin.getLanguageAccessor()).build();
    }
}
