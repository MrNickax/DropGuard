package com.nickax.dropguard.command.messages;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.language.LanguageManager;
import com.nickax.genten.command.messages.CommandMessages;
import com.nickax.genten.message.Message;
import com.nickax.genten.message.MessageList;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DropGuardCommandMessages extends CommandMessages {

   private final LanguageManager languageManager;

    public DropGuardCommandMessages(DropGuard plugin) {
        this.languageManager = plugin.getLanguageManager();
    }

    @Override
    public String getNoPermissionMessage(CommandSender sender) {
        return languageManager.getMessage("no-permission", Message.class, sender).getValue();
    }

    @Override
    public String getInvalidCommandSenderMessage(CommandSender sender) {
        return languageManager.getMessage("invalid-command-sender", Message.class, sender).getValue();
    }

    @Override
    public String getInvalidPageMessage(CommandSender sender) {
        return languageManager.getMessage("invalid-page", Message.class, sender).getValue();
    }

    @Override
    public String getCommandFormat(CommandSender sender) {
        return languageManager.getMessage("command-format", Message.class, sender).getValue();
    }

    @Override
    public List<String> getHelpFormat(CommandSender sender) {
        return languageManager.getMessage("help-format", MessageList.class, sender).getValue();
    }

    @Override
    public String getHelpNotFoundMessage(CommandSender commandSender) {
        return languageManager.getMessage("help-not-found", Message.class, commandSender).getValue();
    }
}
