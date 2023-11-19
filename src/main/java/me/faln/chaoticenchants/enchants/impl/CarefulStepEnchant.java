package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CarefulStepEnchant extends AbstractEnchant {

    public CarefulStepEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("carefulstep"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerInteractEvent.class)
                .filter(event -> event.getAction() == Action.PHYSICAL)
                .filter(PlayerInteractEvent::hasBlock)
                .filter(event -> event.getClickedBlock().getType() == Material.FARMLAND)
                .filter(event -> Metadata.provideForPlayer(event.getPlayer()).has(this.metadataKey))
                .handler(event -> event.setCancelled(true))
                .bindWith(consumer);
    }
}
