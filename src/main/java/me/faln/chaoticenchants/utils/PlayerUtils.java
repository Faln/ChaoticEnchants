package me.faln.chaoticenchants.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public final class PlayerUtils {

    public static void giveItem(final Player player, final ItemStack itemStack) {
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
            return;
        }

        player.getInventory().addItem(itemStack);
    }

}
