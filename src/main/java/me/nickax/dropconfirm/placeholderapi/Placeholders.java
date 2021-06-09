package me.nickax.dropconfirm.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {


    private final DropConfirm plugin;

    public Placeholders(DropConfirm plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "dropconfirm";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if (player == null) {
            return "Player not found!";
        }

        if(identifier.equals("enabled")){
            if (plugin.dataManager.getEnabled().getOrDefault(player.getUniqueId(), true)) {
                return "true";
            } else {
                return "false";
            }
        }

        return null;
    }
}