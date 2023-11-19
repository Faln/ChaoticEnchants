package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.ThreadLocalRandom;

public final class ArrowStormEnchant extends AbstractEnchant {

    public ArrowStormEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("arrowstorm"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(EntityDamageByEntityEvent.class)
                .filter(event -> event.getDamager() instanceof Player)
                .filter(event -> event.getEntity() instanceof Player)
                .filter(event -> Metadata.provideForPlayer(event.getDamager().getUniqueId()).has(this.metadataKey))
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel((Player) event.getDamager())))
                .handler(event -> {
                    final Player player = (Player) event.getDamager();
                    final int level = Metadata.provideForPlayer(player)
                            .get(this.metadataKey)
                            .orElseThrow(IllegalArgumentException::new);
                    final Location location = player.getLocation().clone();
                    player.launchProjectile(Arrow.class, location.getDirection(), arrow -> {
                        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                        arrow.setDamage(1.5);
                    });

                    for (int i = 0; i < level; i++) {
                        final float random = ThreadLocalRandom.current().nextFloat();
                        location.setYaw(location.getYaw() + random);
                        player.launchProjectile(Arrow.class, location.getDirection(), arrow -> {
                            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                            arrow.setDamage(1.5);
                        });
                    }

                    player.sendMessage(super.procMessage);

                }).bindWith(consumer);
    }
}
