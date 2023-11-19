package me.faln.chaoticenchants.enchants;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.rarity.Rarity;
import me.faln.chaoticenchants.utils.Color;
import me.faln.chaoticenchants.utils.EquipmentType;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.metadata.MetadataKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PROTECTED) @Getter
public abstract class AbstractEnchant implements ChaoticEnchant {

    protected final ChaoticEnchants plugin;
    protected final MetadataKey<Integer> metadataKey;
    protected final String id;

    protected final boolean enabled;
    protected final Rarity rarity;
    protected final String displayName;
    protected final List<String> description;
    protected final double[] levels;

    protected final Set<EquipmentType> applicableTypes;
    protected final String procMessage;

    protected AbstractEnchant(final ChaoticEnchants plugin, final ConfigurationSection section) {
        this.plugin = plugin;
        this.id = section.getName();
        this.enabled = section.getBoolean("enabled");
        this.rarity = this.plugin.getRarityRegistry().get(section.getString("rarity"));
        this.displayName = Color.colorize(section.getString("name"));
        this.description = Color.colorize(section.getStringList("description"));
        this.levels = this.parseLevels(section);
        this.metadataKey = MetadataKey.createIntegerKey(this.id);
        this.applicableTypes = this.parseApplicableTypes(section);
        this.procMessage = section.contains("proc-message") ? Color.colorize(section.getString("proc-message")) : null;
    }

    protected double getChanceFromLevel(final Player player) {
        final int level = Metadata.provideForPlayer(player)
                .get(this.metadataKey)
                .orElseThrow(IllegalArgumentException::new);

        return this.levels[level - 1];
    }

    protected double getChanceFromNBT(final ItemStack itemStack) {
        final ReadableNBT nbt = NBT.readNbt(itemStack);

        if (!nbt.hasTag(this.id)) {
            return 0.0;
        }

        final int level = nbt.getInteger(this.id) - 1;

        return this.levels[level];
    }

    private Set<EquipmentType> parseApplicableTypes(final ConfigurationSection section) {
        return section.getStringList("applies-to").stream()
                .map(EquipmentType::match)
                .collect(Collectors.toSet());
    }

    private double[] parseLevels(final ConfigurationSection section) {
        final Set<String> keys = section.getConfigurationSection("levels").getKeys(false);
        final double[] levels = new double[keys.size()];

        for (final String level : keys) {
            levels[Integer.parseInt(level)] = section.getDouble("levels." + level + ".chance");
        }

        return levels;
    }

    @Override
    public int getMaxLevel() {
        return this.levels.length;
    }

    @Override
    public List<String> getDescription() {
        return new ArrayList<>(this.description);
    }

    @Override
    public void activate(final Player player, final int enchantLevel) {
        Metadata.provideForPlayer(player).put(this.metadataKey, enchantLevel);
    }

    @Override
    public void deactivate(final Player player) {
        Metadata.provideForPlayer(player).remove(this.metadataKey);
    }
}
