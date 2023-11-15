package me.faln.chaoticenchants.utils;

import org.bukkit.inventory.ItemStack;

public enum EquipmentType {

    HELMETS,
    CHESTPLATES,
    LEGGINGS,
    BOOTS,
    SWORDS,
    AXES,
    SHOVELS,
    PICKAXES,
    HOES,
    BOWS,
    UNKNOWN;

    public static EquipmentType match(final String string) throws IllegalArgumentException {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Invalid equipment type");
        }

        switch (string.toUpperCase()) {
            case "HELMETS":
                return HELMETS;
            case "CHESTPLATES":
                return CHESTPLATES;
            case "LEGGINGS":
                return LEGGINGS;
            case "BOOTS":
                return BOOTS;
            case "SWORDS":
                return SWORDS;
            case "AXES":
                return AXES;
            case "SHOVELS":
                return SHOVELS;
            case "PICKAXES":
                return PICKAXES;
            case "BOWS":
                return BOWS;
            case "HOES":
                return HOES;
            default:
                throw new IllegalArgumentException("Invalid equipment type");
        }
    }

    public static EquipmentType match(final ItemStack itemStack) {

        if (isHelmet(itemStack)) {
            return HELMETS;
        } else if (isChestplate(itemStack)) {
            return CHESTPLATES;
        } else if (isLeggings(itemStack)) {
            return LEGGINGS;
        } else if (isBoots(itemStack)) {
            return BOOTS;
        } else if (isSwords(itemStack)) {
            return SWORDS;
        } else if (isAxes(itemStack)) {
            return AXES;
        } else if (isPickaxe(itemStack)) {
            return PICKAXES;
        } else if (isShovel(itemStack)) {
            return SHOVELS;
        } else if (isHoe(itemStack)) {
            return HOES;
        } else if (isBow(itemStack)) {
            return BOWS;
        } else {
            return UNKNOWN;
        }
    }

    public static boolean isHelmet(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_HELMET");
    }

    public static boolean isChestplate(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_CHESTPLATE");
    }

    public static boolean isLeggings(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_LEGGINGS");
    }

    public static boolean isBoots(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_BOOTS");
    }

    public static boolean isSwords(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_SWORD");
    }

    public static boolean isAxes(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_AXE");
    }

    public static boolean isPickaxe(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_PICKAXE");
    }

    public static boolean isShovel(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_SHOVEL");
    }

    public static boolean isHoe(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_HOE");
    }

    public static boolean isBow(final ItemStack itemStack) {
        return itemStack.getType().name().endsWith("_BOW");
    }
}
