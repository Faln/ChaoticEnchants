package me.faln.chaoticenchants.enchants.manager;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import lombok.AllArgsConstructor;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.PassiveEvent;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.enchants.registry.EnchantRegistry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public final class EnchantManager {

    private final ChaoticEnchants plugin;

    public void activateEnchants(final ItemStack item, final Player player) {
        final EnchantRegistry enchantRegistry = this.plugin.getEnchantRegistry();
        final ReadWriteNBT nbt = NBT.itemStackToNBT(item);

        for (final String enchantId : enchantRegistry.keySet()) {
            if (!nbt.hasTag(enchantId)) {
                continue;
            }

            final AbstractEnchant enchant = enchantRegistry.get(enchantId);

            if (enchant instanceof PassiveEvent) {
                ((PassiveEvent) enchant).handle(player);
            }

        }
    }

}
