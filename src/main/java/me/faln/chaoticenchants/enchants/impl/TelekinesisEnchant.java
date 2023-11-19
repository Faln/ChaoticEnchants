package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
public final class TelekinesisEnchant  extends AbstractEnchant {

    public TelekinesisEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("telekinesis"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(BlockBreakEvent.class)
                .filter(event -> NBT.readNbt(event.getPlayer().getInventory().getItemInMainHand()).hasTag("telekinesis"))
                .filter(event -> ChanceUtils.parse(this.getChanceFromNBT(event.getPlayer().getInventory().getItemInMainHand())))
                .handler(event -> {
                    final Player player = event.getPlayer();
                    final Block block = event.getBlock();

                    event.setDropItems(false);

                    block.getDrops(player.getInventory().getItemInMainHand()).forEach(item -> {
                        player.getInventory().addItem(item);
                    });
                }).bindWith(consumer);
    }
}
