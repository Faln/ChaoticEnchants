package me.faln.chaoticenchants.enchants.manager;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import lombok.AllArgsConstructor;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.enchants.builder.EnchantBookBuilder;
import me.faln.chaoticenchants.enchants.impl.RunicObstructionEnchant;
import me.faln.chaoticenchants.enchants.registry.EnchantRegistry;
import me.faln.chaoticenchants.rarity.Rarity;
import me.faln.chaoticenchants.utils.Color;
import me.lucko.helper.metadata.Metadata;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class EnchantManager {

    private final ChaoticEnchants plugin;

    public List<ChaoticEnchant> getEnchants(final ItemStack itemStack) {
        final ReadableNBT nbt = NBT.readNbt(itemStack);
        final EnchantRegistry enchantRegistry = this.plugin.getEnchantRegistry();

        return enchantRegistry.keySet().stream()
                .filter(nbt::hasTag)
                .map(enchantRegistry::get)
                .collect(Collectors.toList());
    }

    public ItemStack increaseSuccess(final int toAdd, final ItemStack itemStack) {
        final ReadableNBT nbt = NBT.readNbt(itemStack);

        if (!nbt.hasTag("enchant-book")) {
            throw new IllegalArgumentException("Cannot add success to non-enchantment books");
        }

        final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().get(nbt.getString("enchant-book"));
        final int success = Math.min(nbt.getInteger("enchant-success") + toAdd, 100);
        final int destroy = nbt.getInteger("enchant-destroy");
        final int failure = nbt.getInteger("enchant-failure");
        final int level = nbt.getInteger("enchant-level");

        return new EnchantBookBuilder(enchant)
                .amount(1)
                .level(level)
                .success(success)
                .destroy(destroy)
                .failure(failure)
                .build();
    }

    public ItemStack getUnidentifiedEnchant(final Rarity rarity) {
        final ItemStack item =  rarity.getUnidentifiedItem().build();

        NBT.modify(item, nbt -> {
            nbt.setString("unidentified-book", rarity.getId());
        });

        return item;
    }

    public void applyEnchant(final ItemStack item, final ChaoticEnchant enchant, final int level) {
        NBT.modify(item, nbt -> {
            nbt.setInteger(enchant.getId(), level);
            nbt.modifyMeta((readableNBT, itemMeta) -> {
                this.transformLore(itemMeta, enchant, level);
            });
        });
    }

    public void removeEnchant(final ItemStack item, final ChaoticEnchant enchant) {
        NBT.modify(item, nbt -> {
            nbt.removeKey(enchant.getId());
            nbt.modifyMeta((readableNBT, meta) -> {
                this.removeEnchant(item, enchant);
            });
        });
    }

    public void removeLore(final ItemStack itemStack, final ChaoticEnchant enchant) {
        if (itemStack == null) {
            return;
        }

        final ItemMeta meta = itemStack.getItemMeta();

        if (meta == null || meta.getLore() == null) {
            return;
        }

        final List<String> lore = meta.getLore();
        final List<String> newLore = new LinkedList<>();
        final String enchantToRemove = enchant.getDisplayName()
                .replace("%rarity-color%", enchant.getRarity().getColor())
                .replace("&", "§");

        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).startsWith(enchantToRemove)) {
                continue;
            }

            newLore.add(lore.get(i));
        }

        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
    }

    public void transformLore(final ItemMeta meta, final ChaoticEnchant enchant, final int level) {
        if (meta == null) {
            return;
        }

        final List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
        final String loreAttachment = Color.colorize(enchant.getDisplayName()
                .replace("%rarity-color%", enchant.getRarity().getColor()) + " " + enchant.getRarity().getColor() + level);

        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains(enchant.getDisplayName())) {
                lore.set(i, loreAttachment);
                meta.setLore(lore);
                return;
            }
        }

        lore.add(loreAttachment);
        meta.setLore(lore);
    }

    public void activateEnchants(final ItemStack item, final Player player) {

        if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
            return;
        }

        if (Metadata.provideForEntity(player).has(RunicObstructionEnchant.SILENCED_KEY)) {
            return;
        }

        final EnchantRegistry enchantRegistry = this.plugin.getEnchantRegistry();
        final ReadableNBT nbt = NBT.readNbt(item);

        for (final String enchantId : enchantRegistry.keySet()) {
            if (!nbt.hasTag(enchantId)) {
                continue;
            }

            final ChaoticEnchant enchant = enchantRegistry.get(enchantId);

            if (!enchant.isEnabled()) {
                continue;
            }

            final int enchantLevel = nbt.getInteger(enchantId);

            enchant.activate(player, enchantLevel);
        }
    }

    public void deactivateEnchants(final ItemStack item, final Player player) {

        if (item == null || item.getType() == Material.AIR || item.getAmount() == 0) {
            return;
        }

        final EnchantRegistry enchantRegistry = this.plugin.getEnchantRegistry();
        final ReadableNBT nbt = NBT.readNbt(item);

        for (final String enchantId : enchantRegistry.keySet()) {
            if (!nbt.hasTag(enchantId)) {
                continue;
            }

            final ChaoticEnchant enchant = enchantRegistry.get(enchantId);

            enchant.deactivate(player);
        }
    }

}
