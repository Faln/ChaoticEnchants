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
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public final class WellFedEnchant extends AbstractEnchant {

    private final Map<Integer, Double> feedAmount = new HashMap<>();

    public WellFedEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("wellfed"));

        for (final String level : config.section("wellfed.levels").getKeys(false)) {
            this.feedAmount.put(Integer.parseInt(level), config.parseDouble("wellfed.levels." + level + ".feed-amount"));
        }
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(EntityDamageByEntityEvent.class)
                .filter(event -> event.getDamager() instanceof Player)
                .filter(event -> Metadata.provideForEntity(event.getDamager().getUniqueId()).has(this.metadataKey))
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel((Player) event.getDamager())))
                .handler(event -> {
                    final int enchantLevel = Metadata.provideForEntity(event.getDamager().getUniqueId())
                            .get(this.metadataKey)
                            .orElseThrow(IllegalStateException::new);
                    final Player player = (Player) event.getDamager();

                    player.setFoodLevel((int) Math.min(player.getFoodLevel(), this.feedAmount.get(enchantLevel)));

                }).bindWith(consumer);
    }
}
