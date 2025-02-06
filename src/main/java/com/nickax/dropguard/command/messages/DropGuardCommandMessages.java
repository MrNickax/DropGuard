package com.nickax.dropguard.command.messages;

import com.nickax.dropguard.DropGuard;
import com.nickax.dropguard.language.LanguageManager;
import com.nickax.genten.command.messages.AbstractCommandMessages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DropGuardCommandMessages extends AbstractCommandMessages {

   private final LanguageManager languageManager;

    public DropGuardCommandMessages(DropGuard plugin) {
        this.languageManager = plugin.getLanguageManager();
    }

    @Override
    public String getNoPermissionMessage(CommandSender sender) {
        return languageManager.getMessage("no-permission", sender).getContent();
    }

    @Override
    public String getInvalidCommandSenderMessage(CommandSender sender) {
        return languageManager.getMessage("invalid-command-sender", sender).getContent();
    }

    @Override
    public String getInvalidPageMessage(CommandSender sender) {
        return languageManager.getMessage("invalid-page", sender).getContent();
    }

    @Override
    public String getCommandFormat(CommandSender sender) {
        return languageManager.getMessage("command-format", sender).getContent();
    }

    @Override
    public List<String> getHelpFormat(CommandSender sender) {
        return languageManager.getMessageList("help-format", sender).getContent();
    }
}
