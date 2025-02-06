package com.nickax.dropguard.item;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ProtectedItem {

    private final String material;
    private final ProtectedItemProperties properties;

    public ProtectedItem(String material, ProtectedItemProperties properties) {
        this.material = material;
        this.properties = properties;
    }

    public boolean doesItemMatch(ItemStack item) {
        return material.equals("ALL") || item.getType().equals(getMaterial()) && properties.doesItemMatch(item);
    }

    public Material getMaterial() {
        return XMaterial.matchXMaterial(material).get().get();
    }
}
