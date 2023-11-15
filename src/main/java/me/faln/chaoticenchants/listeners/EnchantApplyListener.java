package me.faln.chaoticenchants.listeners;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.faln.chaoticenchants.utils.EquipmentType;
import me.faln.chaoticenchants.utils.Replacer;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@SuppressWarnings("all")
public final class EnchantApplyListener implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(InventoryClickEvent.class)
                .filter(event -> event.getClickedInventory() != null)
                .filter(event -> event.getCursor().getType() != Material.AIR)
                .filter(event -> event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)
                .handler(event -> {
                    final Player player = (Player) event.getWhoClicked();
                    final ItemStack item = event.getCurrentItem();
                    final ItemStack cursor = event.getCursor();
                    final ReadableNBT itemNBT = NBT.readNbt(item);
                    final ReadableNBT cursorNBT = NBT.readNbt(cursor);

                    if (!cursorNBT.hasTag("enchant-book")) {
                        return;
                    }

                    if (item.getAmount() > 1) {
                        this.plugin.getLangManager().send(player, "cannot-enchant-stacked-items");
                        return;
                    }

                    final String enchantId = cursorNBT.getString("enchant-book");
                    final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().getOpt(enchantId).orElse(null);

                    if (enchant == null) {
                        return;
                    }

                    final EquipmentType type = EquipmentType.match(item);

                    if (type == EquipmentType.UNKNOWN) {
                        return;
                    }

                    if (!enchant.getApplicableTypes().contains(type)) {
                        this.plugin.getLangManager().send(player, "item-not-applicable");
                        return;
                    }

                    if (itemNBT.hasTag(enchantId)) {
                        final int existingEnchantLevel = itemNBT.getInteger(enchantId);

                        if (existingEnchantLevel == enchant.getMaxLevel()) {
                            this.plugin.getLangManager().send(player, "item-already-maxed");
                            event.setCancelled(true);
                            return;
                        }

                        if (existingEnchantLevel >= cursorNBT.getInteger("enchant-level")) {
                            this.plugin.getLangManager().send(player, "item-already-has-higher-level");
                            event.setCancelled(true);
                            return;
                        }
                    }

                    event.setCancelled(true);

                    if (cursor.getAmount() > 1) {
                        cursor.setAmount(cursor.getAmount() - 1);
                        event.setCursor(cursor);
                    } else {
                        event.setCursor(null);
                    }

                    final double applyChance = cursorNBT.getDouble("enchant-success");

                    if (!ChanceUtils.parse(applyChance)) {

                        final double destroyChance = cursorNBT.getDouble("enchant-destroy");

                        if (!ChanceUtils.parse(destroyChance)) {
                            event.setCurrentItem(null);
                            this.plugin.getLangManager().send(player, "destroy");
                            return;
                        }

                        this.plugin.getLangManager().send(player, "failed");
                        return;
                    }

                    final int level = cursorNBT.getInteger("enchant-level");

                    this.plugin.getEnchantManager().applyEnchant(item, enchant, level);
                    this.plugin.getLangManager().send(player, "success", new Replacer()
                            .add("%enchant%", enchant.getDisplayName().replace("%rarity-color%", enchant.getRarity().getColor()))
                    );

                }).bindWith(consumer);
    }

}
