package me.faln.chaoticenchants.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public final class ChanceUtils {

    public static boolean parse(final double chance) {
        if (chance <= 0.0 || chance > 100.0) {
            return false;
        }

        return ThreadLocalRandom.current().nextInt(100) <= chance;
    }
}
