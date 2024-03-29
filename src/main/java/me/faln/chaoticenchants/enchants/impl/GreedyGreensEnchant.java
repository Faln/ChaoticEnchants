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
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public final class GreedyGreensEnchant extends AbstractEnchant {

    public GreedyGreensEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("greedygreens"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(BlockBreakEvent.class)
                .filter(event -> CropUtils.ALL_CROPS.contains(event.getBlock().getType()))
                .filter(event -> NBT.readNbt(event.getPlayer().getInventory().getItemInMainHand()).hasTag("greedygreens"))
                .filter(event -> this.plugin.getShopGUIHook().isLoaded())
                .filter(event -> ChanceUtils.parse(this.getChanceFromNBT(event.getPlayer().getInventory().getItemInMainHand())))
                .handler(event -> {
                    final Player player = event.getPlayer();
                    final int enchantLevel = NBT.readNbt(player.getInventory().getItemInMainHand()).getInteger("greedygreens") + 1;
                    final double sum = event.getBlock().getDrops().stream()
                            .mapToDouble(itemStack -> this.plugin.getShopGUIHook().getPrice(itemStack))
                            .filter(price -> price != -1)
                            .sum();

                    this.plugin.getVaultHook().add(player, ((enchantLevel * 2.0) / 100.0) * sum);
                })
                .bindWith(consumer);
    }
}
