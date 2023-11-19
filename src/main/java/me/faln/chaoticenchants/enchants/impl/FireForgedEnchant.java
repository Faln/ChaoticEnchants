package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityCombustEvent;

public final class FireForgedEnchant extends AbstractEnchant {

    public FireForgedEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("fireforged"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(EntityCombustEvent.class)
                .filter(event -> event.getEntity() instanceof Item)
                .filter(event -> NBT.readNbt(((Item) event.getEntity()).getItemStack()).hasTag("fireforged"))
                .handler(event -> event.setCancelled(true))
                .bindWith(consumer);
    }
}
