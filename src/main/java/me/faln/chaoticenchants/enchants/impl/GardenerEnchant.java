package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.CropUtils;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.block.BlockBreakEvent;

public final class GardenerEnchant extends AbstractEnchant {

    public GardenerEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("gardener"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(BlockBreakEvent.class)
                .filter(event -> CropUtils.CROPS.containsKey(event.getBlock().getType()))
                .filter(event -> NBT.readNbt(event.getPlayer().getInventory().getItemInMainHand()).hasTag("gardener"))
                .handler(event -> {
                    final Block block = event.getBlock();

                    if (!(block.getBlockData() instanceof Ageable)) {
                        return;
                    }

                    final Ageable ageable = (Ageable) block.getBlockData();

                    if (ageable.getMaximumAge() == ageable.getAge()) {
                        return;
                    }

                    event.setCancelled(true);
                }).bindWith(consumer);
    }
}
