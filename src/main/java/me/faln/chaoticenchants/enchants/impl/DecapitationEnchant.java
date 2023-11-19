package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.faln.chaoticenchants.utils.SkullUtils;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Location;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class DecapitationEnchant extends AbstractEnchant {

    public DecapitationEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("decapitation"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerDeathEvent.class)
                .filter(event -> event.getEntity().getKiller() != null)
                .filter(event -> NBT.readNbt(event.getEntity().getKiller().getInventory().getItemInMainHand()).hasTag("decapitation"))
                .filter(event -> ChanceUtils.parse(this.getChanceFromNBT(event.getEntity().getKiller().getInventory().getItemInMainHand())))
                .handler(event -> {
                    final Location location = event.getEntity().getLocation();

                    if (location.getWorld() == null) {
                        return;
                    }

                    location.getWorld().dropItemNaturally(location, SkullUtils.getSkull(event.getEntity()));

                }).bindWith(consumer);
    }
}
