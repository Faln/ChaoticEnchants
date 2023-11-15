package me.faln.chaoticenchants.enchants.impl;

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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class SnowShoesEnchant extends AbstractEnchant {

    private static final Set<Material> MATERIALS = new HashSet<>(5);
    private final PotionEffect potionEffect;

    static {
        SnowShoesEnchant.MATERIALS.add(Material.ICE);
        SnowShoesEnchant.MATERIALS.add(Material.FROSTED_ICE);
        SnowShoesEnchant.MATERIALS.add(Material.PACKED_ICE);
        SnowShoesEnchant.MATERIALS.add(Material.SNOW);
        SnowShoesEnchant.MATERIALS.add(Material.SNOW_BLOCK);
    }

    public SnowShoesEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("snowshoes"));
        this.potionEffect = new PotionEffect(PotionEffectType.SPEED, 21, config.parseInt("showshoes.speed-level"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerMoveEvent.class)
                .filter(EventFilters.playerHasMetadata(this.metadataKey))
                .filter(EventFilters.ignoreSameBlock())
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel(event.getPlayer())))
                .handler(event -> {
                    final Player player = event.getPlayer();
                    final Block block = player.getLocation().clone().subtract(0.0, 1.0, 0.0).getBlock();

                    if (SnowShoesEnchant.MATERIALS.contains(block.getType())) {
                        player.addPotionEffect(this.potionEffect);
                    }
                }).bindWith(consumer);
        /*Schedulers.builder()
                .sync()
                .every(1, TimeUnit.SECONDS)
                .run(() -> {
                    Metadata.lookupPlayersWithKey(this.metadataKey).keySet().forEach(player -> {

                        final Block block = player.getLocation().clone().subtract(0.0, 1.0, 0.0).getBlock();

                        if (SnowShoesEnchant.MATERIALS.contains(block.getType())) {
                            player.addPotionEffect(this.potionEffect, false);
                        }
                    });
                }).bindWith(consumer);*/
    }
}
