package me.nickax.dropconfirm.dropconfirm;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DropListeners implements Listener {

    private final DropConfirm plugin;
    private final Map<Player, Integer> timer = new HashMap<>();
    private final Map<Player, ItemStack> previous = new HashMap<>();

    public DropListeners(DropConfirm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (plugin.getDataManager().getEnabled().getOrDefault(p.getUniqueId(), true)) {
            if (plugin.getConfig().getBoolean("item-list-enabled")) {
                if (plugin.getConfig().contains("item-list")) {
                    if (plugin.getConfig().contains("item-list." + ".custom-items")) {
                        plugin.getConfig().getConfigurationSection("item-list." + ".custom-items").getKeys(false).forEach(key -> {
                            if (plugin.getConfig().contains("item-list." + "custom-items." + key + ".material")) {
                                ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getConfig().getString("item-list." + "custom-items." + key + ".material")));
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                if (e.getItemDrop().getItemStack().getType() == itemStack.getType()) {
                                    if (plugin.getConfig().contains("item-list." + "custom-items." + key + ".name")) {
                                        if (plugin.getConfig().contains("item-list." + "custom-items." + key + ".name")) {
                                            if (itemMeta != null) {
                                                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("item-list." + "custom-items." + key + ".name"))));
                                            }
                                            itemStack.setItemMeta(itemMeta);
                                        }
                                        if (e.getItemDrop().getItemStack().getItemMeta() != null) {
                                            if (itemStack.getType() == e.getItemDrop().getItemStack().getType()) {
                                                if (plugin.getConfig().contains("item-list." + "custom-items." + key + ".name")) {
                                                    if (itemStack.getItemMeta().getDisplayName().equals(e.getItemDrop().getItemStack().getItemMeta().getDisplayName())) {
                                                        if (plugin.getConfig().getBoolean("drop-options." + "drop-confirm-per-item")) {
                                                            if (timer.containsKey(p)) {
                                                                if (e.getItemDrop().getItemStack().equals(previous.get(p))) {
                                                                    e.setCancelled(false);
                                                                } else {
                                                                    plugin.getMessageManager().dropConfirm(p);
                                                                    e.setCancelled(true);
                                                                    previous.put(p, e.getItemDrop().getItemStack());
                                                                }
                                                            } else {
                                                                plugin.getMessageManager().dropConfirm(p);
                                                                e.setCancelled(true);
                                                                previous.put(p, e.getItemDrop().getItemStack());
                                                                dropTask(p);
                                                            }
                                                        } else {
                                                            if (timer.containsKey(p)) {
                                                                e.setCancelled(false);
                                                            } else {
                                                                plugin.getMessageManager().dropConfirm(p);
                                                                e.setCancelled(true);
                                                                dropTask(p);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
                if (plugin.getConfig().contains("item-list." + ".normal-items")) {
                    for (String item : plugin.getConfig().getStringList("item-list." + ".normal-items")) {
                        ItemStack drop = e.getItemDrop().getItemStack();
                        if (drop.getItemMeta() != null) {
                            if (drop.getItemMeta().hasDisplayName()) {
                                return;
                            }
                        }
                        if (item.contains("ALL")) {
                            if (plugin.getConfig().getBoolean("drop-options." + "drop-confirm-per-item")) {
                                if (timer.containsKey(p)) {
                                    if (previous.containsKey(p)) {
                                        if (e.getItemDrop().getItemStack().equals(previous.get(p))) {
                                            e.setCancelled(false);
                                        } else {
                                            plugin.getMessageManager().dropConfirm(p);
                                            e.setCancelled(true);
                                            previous.put(p, e.getItemDrop().getItemStack());
                                        }
                                    } else {
                                        plugin.getMessageManager().dropConfirm(p);
                                        e.setCancelled(true);
                                        previous.put(p, e.getItemDrop().getItemStack());
                                    }
                                } else if (!timer.containsKey(p)) {
                                    plugin.getMessageManager().dropConfirm(p);
                                    e.setCancelled(true);
                                    previous.put(p, e.getItemDrop().getItemStack());
                                    dropTask(p);
                                }
                            } else {
                                if (timer.containsKey(p)) {
                                    e.setCancelled(false);
                                } else if (!timer.containsKey(p)) {
                                    plugin.getMessageManager().dropConfirm(p);
                                    e.setCancelled(true);
                                    dropTask(p);
                                }
                            }
                        } else {
                            if (new ItemStack(Material.valueOf(item)).getType().equals(e.getItemDrop().getItemStack().getType())) {
                                if (plugin.getConfig().getBoolean("drop-options." + "drop-confirm-per-item")) {
                                    if (timer.containsKey(p)) {
                                        if (e.getItemDrop().getItemStack().equals(previous.get(p))) {
                                            e.setCancelled(false);
                                        } else {
                                            plugin.getMessageManager().dropConfirm(p);
                                            e.setCancelled(true);
                                            previous.put(p, e.getItemDrop().getItemStack());
                                        }
                                    } else {
                                        plugin.getMessageManager().dropConfirm(p);
                                        e.setCancelled(true);
                                        previous.put(p, e.getItemDrop().getItemStack());
                                        dropTask(p);
                                    }
                                } else {
                                    if (timer.containsKey(p)) {
                                        e.setCancelled(false);
                                    } else {
                                        plugin.getMessageManager().dropConfirm(p);
                                        e.setCancelled(true);
                                        dropTask(p);
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (!plugin.getConfig().getBoolean("item-list-enabled")) {
                if (plugin.getConfig().getBoolean("drop-options." + "drop-confirm-per-item")) {
                    if (timer.containsKey(p)) {
                        if (previous.containsKey(p)) {
                            if (e.getItemDrop().getItemStack().equals(previous.get(p))) {
                                e.setCancelled(false);
                            } else {
                                plugin.getMessageManager().dropConfirm(p);
                                e.setCancelled(true);
                                previous.put(p, e.getItemDrop().getItemStack());
                            }
                        } else {
                            plugin.getMessageManager().dropConfirm(p);
                            e.setCancelled(true);
                            previous.put(p, e.getItemDrop().getItemStack());
                        }
                    } else if (!timer.containsKey(p)) {
                        plugin.getMessageManager().dropConfirm(p);
                        e.setCancelled(true);
                        previous.put(p, e.getItemDrop().getItemStack());
                        dropTask(p);
                    }
                } else {
                    if (timer.containsKey(p)) {
                        e.setCancelled(false);
                    } else if (!timer.containsKey(p)) {
                        plugin.getMessageManager().dropConfirm(p);
                        e.setCancelled(true);
                        dropTask(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (plugin.getConfig().getBoolean("drop-options." + ".drop-confirm-per-item")) {
            if (plugin.getConfig().getBoolean("drop-options." + ".reset-confirm-on-pickup")) {
                ItemStack itemStack = e.getItem().getItemStack();
                if (previous.get(p) != null) {
                    if (previous.get(p).getType().equals(itemStack.getType())) {
                        if (itemStack.getItemMeta() != null && previous.get(p).getItemMeta() != null) {
                            if (itemStack.getItemMeta().hasDisplayName() && previous.get(p).getItemMeta().hasDisplayName()) {
                                if (itemStack.getItemMeta().getDisplayName().equals(previous.get(p).getItemMeta().getDisplayName())) {
                                    previous.remove(p);
                                }
                            } else {
                                previous.remove(p);
                            }
                        } else {
                            previous.remove(p);
                        }
                    }
                }
            }
        }
    }

    private void dropTask(Player player) {
        timer.put(player, plugin.getConfig().getInt("drop-options." + "drop-timer"));
        new BukkitRunnable() {
            public void run() {
                timer.put(player, timer.get(player) - 1);
                if (timer.get(player) <= 0) {
                    timer.remove(player);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
