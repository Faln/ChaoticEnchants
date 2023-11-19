package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.Schedulers;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;

import java.util.concurrent.TimeUnit;

public final class LifeforceEnchant extends AbstractEnchant {

    public LifeforceEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("lifeforce"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Schedulers.builder()
                .sync()
                .every(5, TimeUnit.SECONDS)
                .run(() -> {
                    Metadata.lookupPlayersWithKey(this.metadataKey).forEach((player, value) -> {
                        final int level = value + 1;

                        player.setHealth(player.getHealth() + level);
                        player.setFoodLevel(player.getFoodLevel() + level);
                    });
                }).bindWith(consumer);
    }

}
