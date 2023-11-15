package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

public final class ClarityEnchant extends AbstractEnchant {

    public ClarityEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("clarity"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(EntityPotionEffectEvent.class)
                .filter(event -> event.getEntity() instanceof Player)
                .filter(event -> event.getModifiedType().equals(PotionEffectType.BLINDNESS))
                .filter(event -> Metadata.provideForPlayer(event.getEntity().getUniqueId()).has(this.metadataKey))
                .handler(event -> event.setCancelled(true))
                .bindWith(consumer);
    }
}
