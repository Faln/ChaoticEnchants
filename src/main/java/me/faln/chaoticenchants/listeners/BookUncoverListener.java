package me.faln.chaoticenchants.listeners;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import lombok.AllArgsConstructor;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.enchants.builder.EnchantBookBuilder;
import me.faln.chaoticenchants.rarity.Rarity;
import me.faln.chaoticenchants.utils.Replacer;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public final class BookUncoverListener implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@NotNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerInteractEvent.class)
                .filter(event -> event.getHand() == EquipmentSlot.HAND)
                .filter(event -> event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                .filter(PlayerInteractEvent::hasItem)
                .filter(event -> event.getItem().getType() != Material.AIR)
                .filter(event -> NBT.readNbt(event.getItem()).hasTag("unidentified-book"))
                .handler(event -> {
                    final ItemStack item = event.getItem();
                    final Player player = event.getPlayer();
                    final Rarity rarity = this.plugin.getRarityRegistry().get(NBT.readNbt(item).getString("unidentified-book"));
                    final ChaoticEnchant enchant = this.plugin.getEnchantRegistry()
                            .getRandom(rarity)
                            .orElseThrow(IllegalArgumentException::new);
                    final ItemStack book = new EnchantBookBuilder(enchant)
                            .amount(1)
                            .success(ThreadLocalRandom.current().nextInt(101))
                            .failure(ThreadLocalRandom.current().nextInt(101))
                            .destroy(ThreadLocalRandom.current().nextInt(101))
                            .level(ThreadLocalRandom.current().nextInt(enchant.getMaxLevel() + 1))
                            .build();

                    this.plugin.getLangManager().send(player, "enchant-uncovered", new Replacer()
                            .add("%enchant%", book.getItemMeta().getDisplayName())
                    );

                    item.setAmount(item.getAmount() - 1);

                    if (player.getInventory().firstEmpty() == -1) {
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), book);
                        return;
                    }

                    player.getInventory().addItem(book);

                }).bindWith(consumer);
    }
}
