package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.faln.chaoticenchants.utils.CropUtils;
import me.lucko.helper.Events;
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
                .filter(event -> NBT.readNbt(event.getPlayer().getInventory().getItemInMainHand()).hasTag("herbalist"))
                .filter(event -> ChanceUtils.parse(this.getChanceFromNBT(event.getPlayer().getInventory().getItemInMainHand())))
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
