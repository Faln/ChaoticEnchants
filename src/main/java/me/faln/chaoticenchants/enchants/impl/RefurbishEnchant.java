package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class RefurbishEnchant extends AbstractEnchant {

    public RefurbishEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("refurbish"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerItemBreakEvent.class)
                .filter(event -> NBT.readNbt(event.getBrokenItem()).hasTag("refurbish"))
                .filter(event -> Metadata.provideForPlayer(event.getPlayer()).has(this.metadataKey))
                .handler(event -> {
                    final Player player = event.getPlayer();
                    final ItemStack item = event.getBrokenItem().clone();
                    final int enchantLevel = Metadata.provideForPlayer(player)
                            .get(this.metadataKey)
                            .orElseThrow(IllegalStateException::new) - 1;

                    if (enchantLevel == 0) {
                        return;
                    }

                    if (!(item.getItemMeta() instanceof Damageable)) {
                        return;
                    }

                    final Damageable damageable = (Damageable) item.getItemMeta();

                    damageable.setDamage(item.getType().getMaxDurability());
                    item.setItemMeta(damageable);

                    final List<ChaoticEnchant> enchantsList = this.plugin.getEnchantManager().getEnchants(item).stream()
                            .filter(enchant -> !enchant.getId().equals(this.id))
                            .collect(Collectors.toList());

                    this.plugin.getEnchantManager().removeEnchant(item, enchantsList.get(ThreadLocalRandom.current().nextInt(enchantsList.size())));
                    this.plugin.getEnchantManager().removeEnchant(item, this);
                    this.plugin.getEnchantManager().applyEnchant(item, this, enchantLevel);

                    Schedulers.sync().runLater(() -> {
                        player.getInventory().addItem(item);
                    }, 1L);

                }).bindWith(consumer);
    }
}
