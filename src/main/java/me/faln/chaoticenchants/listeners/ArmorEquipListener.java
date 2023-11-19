package me.faln.chaoticenchants.listeners;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.manager.EnchantManager;
import me.faln.chaoticenchants.utils.ArmorEquipEvent;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public final class ArmorEquipListener implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        final EnchantManager enchantManager = this.plugin.getEnchantManager();

        Events.subscribe(ArmorEquipEvent.class)
                .handler(event -> {
                    final Player player = event.getPlayer();

                    enchantManager.activateEnchants(event.getNewArmorPiece(), player);
                    enchantManager.deactivateEnchants(event.getOldArmorPiece(), player);

                }).bindWith(consumer);
    }
}
