package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.faln.chaoticenchants.utils.CropUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class HerbalistEnchant extends AbstractEnchant {

    public HerbalistEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("herbalist"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(BlockBreakEvent.class)
                .filter(event -> CropUtils.ALL_CROPS.contains(event.getBlock().getType()))
                .filter(event -> Metadata.provideForPlayer(event.getPlayer()).has(this.metadataKey))
                .filter(event -> this.plugin.getShopGUIHook().isLoaded())
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel(event.getPlayer())))
                .handler(event -> {
                    final List<ItemStack> drops = new ArrayList<>(event.getBlock().getDrops());
                    final Location location = event.getBlock().getLocation();

                    drops.forEach(item -> {
                        location.getWorld().dropItemNaturally(location, item);
                    });

                })
                .bindWith(consumer);
    }
}
