package me.nickax.dropconfirm.drop.listeners;

import me.nickax.dropconfirm.DropConfirm;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DropListeners implements Listener {

    private final DropConfirm plugin;

    public DropListeners(DropConfirm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (plugin.getPlayerManager().getPlayer(player) != null) {
            if (plugin.getPlayerManager().getPlayer(player).getEnabled()) {
                if (plugin.getConfigManager().config().getBoolean("item-list." + ".enabled")) {
                    if (plugin.getConfigManager().config().contains("item-list." + ".custom-items")) {
                        ConfigurationSection customItems = plugin.getConfigManager().config().getConfigurationSection("item-list." + ".custom-items");
                        if (customItems != null) {
                            customItems.getKeys(false).forEach(item -> {
                                if (plugin.getConfig().contains("item-list." + "custom-items." + item + ".material")) {
                                    ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getConfig().getString("item-list." + "custom-items." + item + ".material")));
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    if (plugin.getConfig().contains("item-list." + "custom-items." + item + ".name")) {
                                        if (itemMeta != null) {
                                            String name = plugin.getConfig().getString("item-list." + "custom-items." + item + ".name");
                                            if (name != null) {
                                                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                                            }
                                            itemStack.setItemMeta(itemMeta);
                                        }
                                    }
                                    if (e.getItemDrop().getItemStack().getType() == itemStack.getType()) {
                                        if (e.getItemDrop().getItemStack().getItemMeta() != null) {
                                            if (itemMeta != null) {
                                                if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(itemMeta.getDisplayName())) {
                                                    if (plugin.getConfigManager().config().getBoolean("drop." + ".confirm-per-item")) {
                                                        if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                                                            if (plugin.getDropManager().getPrevious(player) != null) {
                                                                if (e.getItemDrop().getItemStack().equals(plugin.getDropManager().getPrevious(player))) {
                                                                    e.setCancelled(false);
                                                                } else {
                                                                    plugin.getMessageManager().dropConfirm(player);
                                                                    e.setCancelled(true);
                                                                    plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                                                }
                                                            } else {
                                                                plugin.getMessageManager().dropConfirm(player);
                                                                e.setCancelled(true);
                                                                plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                                            }
                                                        } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                                                            plugin.getMessageManager().dropConfirm(player);
                                                            e.setCancelled(true);
                                                            plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                                            plugin.getDropManager().dropTask(player);
                                                        }
                                                    } else {
                                                        if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                                                            e.setCancelled(false);
                                                        } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                                                            plugin.getMessageManager().dropConfirm(player);
                                                            e.setCancelled(true);
                                                            plugin.getDropManager().dropTask(player);
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
                    if (plugin.getConfigManager().config().contains("item-list." + ".normal-items")) {
                        ItemStack drop = e.getItemDrop().getItemStack();
                        if (drop.getItemMeta() != null) {
                            if (drop.getItemMeta().hasDisplayName()) {
                                return;
                            }
                        }
                        if (plugin.getConfigManager().config().getStringList("item-list." + ".normal-items").contains("ALL")) {
                            if (plugin.getConfigManager().config().getBoolean("drop." + ".confirm-per-item")) {
                                if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                                    if (plugin.getDropManager().getPrevious(player) != null) {
                                        if (e.getItemDrop().getItemStack().equals(plugin.getDropManager().getPrevious(player))) {
                                            e.setCancelled(false);
                                        } else {
                                            plugin.getMessageManager().dropConfirm(player);
                                            e.setCancelled(true);
                                            plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                        }
                                    } else {
                                        plugin.getMessageManager().dropConfirm(player);
                                        e.setCancelled(true);
                                        plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                    }
                                } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                                    plugin.getMessageManager().dropConfirm(player);
                                    e.setCancelled(true);
                                    plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                    plugin.getDropManager().dropTask(player);
                                }
                            } else {
                                if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                                    e.setCancelled(false);
                                } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                                    plugin.getMessageManager().dropConfirm(player);
                                    e.setCancelled(true);
                                    plugin.getDropManager().dropTask(player);
                                }
                            }
                        } else {
                            for (String item : plugin.getConfig().getStringList("item-list." + ".normal-items")) {
                                ItemStack itemStack = new ItemStack(Material.valueOf(item));
                                if (itemStack.getType().equals(drop.getType())) {
                                    if (plugin.getConfigManager().config().getBoolean("drop." + ".confirm-per-item")) {
                                        if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                                            if (plugin.getDropManager().getPrevious(player) != null) {
                                                if (e.getItemDrop().getItemStack().equals(plugin.getDropManager().getPrevious(player))) {
                                                    e.setCancelled(false);
                                                } else {
                                                    plugin.getMessageManager().dropConfirm(player);
                                                    e.setCancelled(true);
                                                    plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                                }
                                            } else {
                                                plugin.getMessageManager().dropConfirm(player);
                                                e.setCancelled(true);
                                                plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                            }
                                        } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                                            plugin.getMessageManager().dropConfirm(player);
                                            e.setCancelled(true);
                                            plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                            plugin.getDropManager().dropTask(player);
                                        }
                                    } else {
                                        if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                                            e.setCancelled(false);
                                        } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                                            plugin.getMessageManager().dropConfirm(player);
                                            e.setCancelled(true);
                                            plugin.getDropManager().dropTask(player);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (!plugin.getConfigManager().config().getBoolean("item-list." + ".enabled")) {
                    if (plugin.getConfigManager().config().getBoolean("drop." + ".confirm-per-item")) {
                        if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                            if (plugin.getDropManager().getPrevious(player) != null) {
                                if (e.getItemDrop().getItemStack().equals(plugin.getDropManager().getPrevious(player))) {
                                    e.setCancelled(false);
                                } else {
                                    plugin.getMessageManager().dropConfirm(player);
                                    e.setCancelled(true);
                                    plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                                }
                            } else {
                                plugin.getMessageManager().dropConfirm(player);
                                e.setCancelled(true);
                                plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                            }
                        } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                            plugin.getMessageManager().dropConfirm(player);
                            e.setCancelled(true);
                            plugin.getDropManager().setPrevious(player, e.getItemDrop().getItemStack());
                            plugin.getDropManager().dropTask(player);
                        }
                    } else {
                        if (plugin.getDropManager().getTimerMap().containsKey(player)) {
                            e.setCancelled(false);
                        } else if (!plugin.getDropManager().getTimerMap().containsKey(player)) {
                            plugin.getMessageManager().dropConfirm(player);
                            e.setCancelled(true);
                            plugin.getDropManager().dropTask(player);
                        }
                    }
                }
            }
        }
    }
}
