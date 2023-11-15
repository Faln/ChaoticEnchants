package me.faln.chaoticenchants.enchants;

import me.faln.chaoticenchants.rarity.Rarity;
import me.faln.chaoticenchants.utils.EquipmentType;
import me.lucko.helper.metadata.MetadataKey;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public interface ChaoticEnchant extends TerminableModule {

    void activate(final Player player, final int enchantLevel);

    void deactivate(final Player player);

    boolean isEnabled();

    int getMaxLevel();

    double[] getLevels();

    Rarity getRarity();

    String getDisplayName();

    String getId();

    List<String> getDescription();

    MetadataKey<Integer> getMetadataKey();

    Set<EquipmentType> getApplicableTypes();

}
