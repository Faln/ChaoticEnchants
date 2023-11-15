package me.faln.chaoticenchants.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@UtilityClass
public final class NumberUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

    public static String formatExp(final Player player) {
        return DECIMAL_FORMAT.format(player.getTotalExperience());
    }

    public static String formatExp(final int amount) {
        return DECIMAL_FORMAT.format(amount);
    }

    private static int getExpAtLevel(Player player) {
        return getExpAtLevel(player.getLevel());
    }

    public static int getExpAtLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        }

        if (level <= 30) {
            return 5 * level - 38;
        }

        return 9 * level - 158;
    }

    public static int getTotalExperience(Player player) {
        int exp = Math.round(getExpAtLevel(player) * player.getExp());
        int currentLevel = player.getLevel();

        while (currentLevel > 0) {
            currentLevel--;
            exp += getExpAtLevel(currentLevel);
        }

        if (exp < 0) exp = Integer.MAX_VALUE;

        return exp;
    }

}
