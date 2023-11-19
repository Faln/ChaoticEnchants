package me.faln.chaoticenchants.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;

import java.util.*;

@UtilityClass
public final class CropUtils {

    public static final Map<Material, Integer> CROPS = new EnumMap<>(Material.class);
    public static final Set<Material> ALL_CROPS = new HashSet<>();

    static {
        CROPS.put(Material.WHEAT_SEEDS, 7);
        CROPS.put(Material.WHEAT, 7);
        CROPS.put(Material.CARROT, 7);
        CROPS.put(Material.POTATOES, 7);
        CROPS.put(Material.NETHER_WART, 3);
        CROPS.put(Material.BEETROOT, 3);
        CROPS.put(Material.SUGAR_CANE, 15);

        ALL_CROPS.addAll(CROPS.keySet());
        ALL_CROPS.addAll(EnumSet.of(Material.MELON, Material.PUMPKIN));
    }

}
