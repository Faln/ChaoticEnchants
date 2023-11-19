package me.faln.chaoticenchants.enchants.impl;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class DeathGripEnchant extends AbstractEnchant {

    private final ListMultimap<UUID, ItemStack> items = MultimapBuilder
            .hashKeys()
            .arrayListValues()
            .build();

    public DeathGripEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("deathgrip"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerDeathEvent.class)
                .handler(event -> {
                    final Player player = event.getPlayer();

                    for (final ItemStack itemStack : player.getInventory().getContents()) {
                        if (itemStack == null || itemStack.getType() == Material.AIR) {
                            continue;
                        }

                        if (!NBT.readNbt(itemStack).hasTag("deathgrip")) {
                            continue;
                        }

                        if (ChanceUtils.parse(this.getChanceFromNBT(itemStack))) {
                            this.items.put(player.getUniqueId(), itemStack);
                            event.getDrops().remove(itemStack);
                        }
                    }

                }).bindWith(consumer);

        Events.subscribe(PlayerRespawnEvent.class)
                .filter(event -> this.items.containsKey(event.getPlayer().getUniqueId()))
                .handler(event -> {
                    final Player player = event.getPlayer();

                    this.items.get(player.getUniqueId()).forEach(item -> {
                        player.getInventory().addItem(item);
                    });

                    this.items.removeAll(player.getUniqueId());
                }).bindWith(consumer);
    }
}
