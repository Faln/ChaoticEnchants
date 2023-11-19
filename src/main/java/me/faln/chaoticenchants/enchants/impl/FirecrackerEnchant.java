package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import jdk.internal.net.http.common.Pair;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.faln.chaoticenchants.utils.FireworkUtils;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class FirecrackerEnchant extends AbstractEnchant {

    private final Map<UUID, Pair<Long, Integer>> pendingTasks = new HashMap<>();
    private final int amountPerLevel;

    public FirecrackerEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("firecracker"));
        this.amountPerLevel = config.parseInt("firecracker.fireworks-per-level");
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {

        Schedulers.builder()
                .sync()
                .every(2, TimeUnit.SECONDS)
                .run(() -> {
                    final Iterator<Map.Entry<UUID, Pair<Long, Integer>>> it = this.pendingTasks.entrySet().iterator();

                    while (it.hasNext()) {
                        final Map.Entry<UUID, Pair<Long, Integer>> entry = it.next();

                        if (System.currentTimeMillis() >= entry.getValue().first) {
                            it.remove();
                            continue;
                        }

                        final Player player = Bukkit.getPlayer(entry.getKey());

                        if (player == null || !player.isOnline() || player.isDead()) {
                            it.remove();
                            continue;
                        }

                        FireworkUtils.spawnFireworks(player.getLocation(), entry.getValue().second);
                    }

                }).bindWith(consumer);

        Events.subscribe(EntityDamageByEntityEvent.class)
                .filter(event -> event.getEntity() instanceof Player)
                .filter(event -> event.getDamager() instanceof Player)
                .filter(event -> Metadata.provideForEntity(event.getDamager()).has(this.metadataKey))
                .filter(event -> !this.pendingTasks.containsKey(event.getEntity().getUniqueId()))
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel((Player) event.getDamager())))
                .handler(event -> {
                    final Player player = (Player) event.getEntity();
                    final Player damager = (Player) event.getDamager();
                    final ItemStack item = damager.getInventory().getItemInMainHand();

                    if (!NBT.readNbt(item).hasTag("firecracker")) {
                        return;
                    }

                    if (this.pendingTasks.containsKey(player.getUniqueId())) {
                        return;
                    }

                    if (!ChanceUtils.parse(this.getChanceFromNBT(item))) {
                        return;
                    }

                    final int enchantLevel = NBT.readNbt(item).getInteger("firecracker");

                    this.pendingTasks.put(
                            player.getUniqueId(),
                            new Pair<>(System.currentTimeMillis() + 6000L, enchantLevel * this.amountPerLevel)
                    );

                }).bindWith(consumer);
    }
}
