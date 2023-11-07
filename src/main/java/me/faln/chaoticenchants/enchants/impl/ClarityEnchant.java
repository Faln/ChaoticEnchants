package me.faln.chaoticenchants.enchants.impl;

import me.faln.chaoticenchants.ChaoticEnchants;
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

public final class ClarityEnchant extends AbstractEnchant {

    private static final MetadataKey<Integer> CLARITY_KEY = MetadataKey.createIntegerKey("CLARITY");

    public ClarityEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("clarity"));
    }


    @Override
    public void setup(@Nonnull final TerminableConsumer consumer) {
        Events.subscribe(EntityDamageByEntityEvent.class)
                .filter(e -> e.getDamager() instanceof Player)
                .filter(e -> e.getEntity() instanceof Player)
                .filter(EventFilters.entityHasMetadata(ClarityEnchant.CLARITY_KEY))
                .handler(event -> ((Player) event.getEntity()).removePotionEffect(PotionEffectType.BLINDNESS))
                .bindWith(consumer);
    }
}
