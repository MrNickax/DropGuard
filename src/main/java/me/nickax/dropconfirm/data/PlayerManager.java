package me.nickax.dropconfirm.data;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, DataManager> playerData;

    public PlayerManager() {
        this.playerData = new HashMap<>();
    }

    public void createData(Player player) {
        DataManager dataManager = getPlayer(player);
        if (dataManager == null) {
            this.playerData.put(player.getUniqueId(), new DataManager(player));
        }
    }

    public DataManager getPlayer(Player player) {
        return playerData.get(player.getUniqueId());
    }

    public Map<UUID, DataManager> getPlayerDataMap() {
        return playerData;
    }
}
