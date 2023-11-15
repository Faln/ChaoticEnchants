package me.faln.chaoticenchants.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@UtilityClass
public final class SkullUtils {

    public static ItemStack getSkull(final Player player) {
        final ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta skullMeta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);

        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(player.getDisplayName());
        skull.setItemMeta(skullMeta);

        return skull;
    }
}
