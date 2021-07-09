package me.nickax.dropconfirm.data;
import org.bukkit.entity.Player;

public class DataManager {

    private final Player player;
    private Boolean enabled = true;

    public DataManager(Player player) {
        this.player = player;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Player getPlayer() {
        return player;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
