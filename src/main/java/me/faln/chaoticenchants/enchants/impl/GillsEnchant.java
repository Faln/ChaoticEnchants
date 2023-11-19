package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityAirChangeEvent;

public final class GillsEnchant extends AbstractEnchant {

    public GillsEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("gills"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(EntityAirChangeEvent.class)
                .filter(event -> event.getEntity() instanceof Player)
                .filter(event -> Metadata.provideForPlayer(event.getEntity().getUniqueId()).has(this.metadataKey))
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel((Player) event.getEntity())))
                .handler(event -> event.setCancelled(true))
                .bindWith(consumer);
    }
}
