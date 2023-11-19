package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.metadata.Empty;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.metadata.MetadataKey;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public final class RunicObstructionEnchant extends AbstractEnchant {

    public static final MetadataKey<Empty> SILENCED_KEY = MetadataKey.createEmptyKey("silenced");

    public RunicObstructionEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("runicobstruction"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {

        Events.subscribe(EntityDamageByEntityEvent.class)
                .filter(event -> event.getEntity() instanceof Player)
                .filter(event -> event.getDamager() instanceof Player)
                .handler(event -> {
                    final Player player = (Player) event.getEntity();
                    final Player damager = (Player) event.getDamager();
                    final ItemStack itemStack = damager.getInventory().getItemInMainHand();

                    if (!NBT.readNbt(itemStack).hasTag("runicobstruction")) {
                        return;
                    }

                    if (!ChanceUtils.parse(this.getChanceFromNBT(itemStack))) {
                        return;
                    }

                    Arrays.stream(player.getInventory().getArmorContents())
                            .forEach(item -> {
                                this.plugin.getEnchantManager().deactivateEnchants(item, player);
                            });

                    Metadata.provideForPlayer(player).put(SILENCED_KEY, Empty.instance());

                    Schedulers.sync()
                            .runLater(() -> {
                                Arrays.stream(player.getInventory().getArmorContents())
                                        .forEach(item -> {
                                            this.plugin.getEnchantManager().activateEnchants(item, player);
                                        });
                            }, 5, TimeUnit.SECONDS)
                            .bindWith(consumer);

                }).bindWith(consumer);
    }
}
