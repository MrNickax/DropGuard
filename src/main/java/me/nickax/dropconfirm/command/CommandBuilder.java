package me.nickax.dropconfirm.command;

import org.bukkit.command.CommandSender;

public abstract class CommandBuilder {

    public abstract String command();
    public abstract String syntax();
    public abstract String permission();
    public abstract String description();
    public abstract String usage(CommandSender sender);

    public abstract void execute(CommandSender sender, String[] args);


}