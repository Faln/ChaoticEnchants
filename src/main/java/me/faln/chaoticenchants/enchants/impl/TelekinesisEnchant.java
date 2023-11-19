package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
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
                .filter(event -> Metadata.provideForPlayer(event.getPlayer()).has(this.metadataKey))
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel(event.getPlayer())))
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
