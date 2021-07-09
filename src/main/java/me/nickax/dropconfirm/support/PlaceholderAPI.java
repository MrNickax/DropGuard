package me.nickax.dropconfirm.support;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final DropConfirm plugin;

    public PlaceholderAPI(DropConfirm plugin) {
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
            if (plugin.getPlayerManager().getPlayer(player) != null) {
                if (plugin.getPlayerManager().getPlayer(player).getEnabled()) {
                    return "true";
                } else {
                    return "false";
                }
            } else {
                return "false";
            }
        }
        return null;
    }
}