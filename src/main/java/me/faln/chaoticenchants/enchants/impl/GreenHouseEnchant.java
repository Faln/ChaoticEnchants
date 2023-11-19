package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.CropUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.utils.Players;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public final class GreenHouseEnchant extends AbstractEnchant {

    public GreenHouseEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("greenhouse"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(BlockGrowEvent.class)
                .filter(event -> CropUtils.ALL_CROPS.contains(event.getBlock().getType()))
                .handler(event -> {
                    final Block block = event.getBlock();

                    if (!(block.getBlockData() instanceof Ageable)) {
                        return;
                    }

                    final Ageable ageable = (Ageable) block.getBlockData();

                    if (ageable.getAge() == ageable.getMaximumAge()) {
                        return;
                    }

                    final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

                    Players.forEachInRange(block.getLocation(), 10, player -> {
                        if (Metadata.provideForPlayer(player).has(this.metadataKey)) {
                            atomicBoolean.set(true);
                        }
                    });

                    if (atomicBoolean.get()) {
                        ageable.setAge(Math.min(ageable.getAge() + 2, ageable.getMaximumAge()));
                    }

                }).bindWith(consumer);
    }
}
