package me.faln.chaoticenchants.enchants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.utils.Color;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PROTECTED) @Getter
public abstract class AbstractEnchant implements TerminableModule {

    protected final ChaoticEnchants plugin;
    protected final boolean enabled;
    protected final int maxLevel;
    protected final String rarity;
    protected final String displayName;
    protected final List<String> description;
    protected final double[] levels;

    protected AbstractEnchant(final ChaoticEnchants plugin, final ConfigurationSection section) {
        this.plugin = plugin;
        this.enabled = section.getBoolean("enabled");
        this.maxLevel = section.getInt("max-level");
        this.rarity = section.getString("rarity");
        this.displayName = Color.colorize(section.getString("name"));
        this.description = Color.colorize(section.getStringList("description"));
        this.levels = this.parseLevels(section);
    }

    private double[] parseLevels(final ConfigurationSection section) {
        final Set<String> keys = section.getConfigurationSection("levels").getKeys(false);
        final double[] levels = new double[keys.size()];

        for (final String level : keys) {
            levels[Integer.parseInt(level) - 1] = section.getDouble("levels." + level + ".chance");
        }

        return levels;
    }
}
