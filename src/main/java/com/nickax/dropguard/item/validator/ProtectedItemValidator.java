package com.nickax.dropguard.item.validator;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.configuration.ConfigurationSection;

public class ProtectedItemValidator {

    public static Result validate(ConfigurationSection section) {
        String material = section.getString("material");

        if (material == null) {
            return Result.MATERIAL_NOT_SPECIFIED;
        }

        if (!material.equals("ALL") && isInvalid(material)) {
            return Result.INVALID_MATERIAL;
        }

        return Result.SUCCESS;
    }

    private static boolean isInvalid(String material) {
        return !XMaterial.matchXMaterial(material).isPresent();
    }

    public enum Result {
        MATERIAL_NOT_SPECIFIED,
        INVALID_MATERIAL,
        SUCCESS
    }
}
