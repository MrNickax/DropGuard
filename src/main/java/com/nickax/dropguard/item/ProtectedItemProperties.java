package com.nickax.dropguard.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ProtectedItemProperties {

    private final String name;
    private final List<String> lore;

    public ProtectedItemProperties(String name, List<String> lore) {
        this.name = name;
        this.lore = lore;
    }

    public boolean doesItemMatch(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        return nameMatch(itemMeta) && loreMatch(itemMeta);
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    private boolean nameMatch(ItemMeta meta) {
        return name == null || meta.hasItemName() && meta.getItemName().equals(name);
    }

    private boolean loreMatch(ItemMeta meta) {
        return lore == null || lore.isEmpty() || meta.hasLore() && meta.getLore().equals(lore);
    }
}
