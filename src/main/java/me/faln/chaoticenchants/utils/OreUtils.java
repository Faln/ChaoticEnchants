package me.faln.chaoticenchants.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.EnumMap;
import java.util.Map;

@UtilityClass
public final class OreUtils {

    public static final Map<Material, ItemStack> ORES = new EnumMap<>(Material.class);

    static {
        ORES.put(Material.COPPER_ORE, new ItemStack(Material.COPPER_INGOT));
        ORES.put(Material.COAL_ORE, new ItemStack(Material.COAL));
        ORES.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT));
        ORES.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        ORES.put(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE));
        ORES.put(Material.LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI));
        ORES.put(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND));
        ORES.put(Material.EMERALD_ORE, new ItemStack(Material.EMERALD));
        ORES.put(Material.DEEPSLATE_COPPER_ORE, new ItemStack(Material.COPPER_INGOT));
        ORES.put(Material.DEEPSLATE_COAL_ORE, new ItemStack(Material.COAL));
        ORES.put(Material.DEEPSLATE_IRON_ORE, new ItemStack(Material.IRON_INGOT));
        ORES.put(Material.DEEPSLATE_GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        ORES.put(Material.DEEPSLATE_REDSTONE_ORE, new ItemStack(Material.REDSTONE));
        ORES.put(Material.DEEPSLATE_LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI));
        ORES.put(Material.DEEPSLATE_DIAMOND_ORE, new ItemStack(Material.DIAMOND));
        ORES.put(Material.DEEPSLATE_EMERALD_ORE, new ItemStack(Material.EMERALD));
    }

}
