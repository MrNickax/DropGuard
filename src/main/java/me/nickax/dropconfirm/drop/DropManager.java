package me.nickax.dropconfirm.drop;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class DropManager {

    private final DropConfirm plugin;
    private final Map<Player, Integer> timer;
    private final Map<Player, ItemStack> previous;

    public DropManager(DropConfirm plugin) {
        this.plugin = plugin;
        this.timer = new HashMap<>();
        this.previous = new HashMap<>();
    }

    public void setPrevious(Player player, ItemStack itemStack) {
        previous.put(player, itemStack);
    }

    public void removePrevious(Player player) {
        previous.remove(player);
    }

    public ItemStack getPrevious(Player player) {
        return previous.getOrDefault(player, null);
    }

    public Map<Player, Integer> getTimerMap() {
        return timer;
    }

    public void dropTask(Player player) {
        timer.put(player, plugin.getConfig().getInt("drop." + "timer"));
        new BukkitRunnable() {
            public void run() {
                timer.put(player, plugin.getDropManager().getTimerMap().get(player) - 1);
                if (timer.get(player) <= 0) {
                    timer.remove(player);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
