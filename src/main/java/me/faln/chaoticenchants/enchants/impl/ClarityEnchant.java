package me.faln.chaoticenchants.enchants.impl;

import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.PassiveEvent;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.registry.YMLConfig;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.event.filter.EventFilters;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.metadata.MetadataKey;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

public final class ClarityEnchant extends AbstractEnchant implements PassiveEvent {

    private static final MetadataKey<Integer> CLARITY_KEY = MetadataKey.createIntegerKey("CLARITY");

    public ClarityEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("clarity"));
    }

    @Override
    public MetadataKey<?> getKey() {
        return ClarityEnchant.CLARITY_KEY;
    }

    @Override
    public void handle(final Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    @Override
    public void setup(@Nonnull TerminableConsumer terminableConsumer) {

    }
}
