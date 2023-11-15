package me.faln.chaoticenchants.enchants.impl;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.event.filter.EventFilters;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class SkyStepperEnchant extends AbstractEnchant {

    private final Map<Integer, Integer> levelsMap = new HashMap<>();
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    public SkyStepperEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("skystepper"));

        for (final String level : config.section("skystepper.levels").getKeys(false)) {
            this.levelsMap.put(Integer.parseInt(level), config.parseInt("skystepper.levels." + level + ".cooldown"));
        }
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Schedulers.builder()
                .sync()
                .every(1, TimeUnit.SECONDS)
                .run(() -> this.cooldownMap.entrySet().removeIf(entry -> System.currentTimeMillis() >= entry.getValue()))
                .bindWith(consumer);

        Events.subscribe(PlayerJumpEvent.class)
                .filter(EventFilters.playerHasMetadata(this.metadataKey))
                .handler(event -> {
                    final Player player = event.getPlayer();
                    final Location location = player.getLocation();

                    if (location.clone().subtract(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR) {
                        return;
                    }

                    if (this.cooldownMap.containsKey(player.getUniqueId())) {
                        return;
                    }

                    if (!ChanceUtils.parse(this.getChanceFromLevel(player))) {
                        return;
                    }

                    final int enchantLevel = Metadata.provideForPlayer(player)
                            .get(this.metadataKey)
                            .orElseThrow(IllegalStateException::new);
                    final int cooldown = this.levelsMap.get(enchantLevel) * 1000;

                    player.setVelocity(location.getDirection().multiply(1).setY(1));
                    this.cooldownMap.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);

                }).bindWith(consumer);
    }
}
