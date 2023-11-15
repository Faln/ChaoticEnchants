package me.faln.chaoticenchants.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

@UtilityClass
public final class FireworkUtils {

    public static void spawnFireworks(final Location location, final int amount) {
        final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        final FireworkMeta meta = firework.getFireworkMeta();

        meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).build());
        meta.setPower(3);
        firework.setFireworkMeta(meta);

        for (int i = 0; i < amount; i++) {
            firework.detonate();
        }
    }

}
