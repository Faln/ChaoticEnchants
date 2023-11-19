package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class WellFedEnchant extends AbstractEnchant {

    private final Map<Integer, Double> feedAmount = new HashMap<>();

    public WellFedEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("wellfed"));

        for (final String level : config.section("wellfed.levels").getKeys(false)) {
            this.feedAmount.put(Integer.parseInt(level), config.parseDouble("wellfed.levels." + level + ".feed-amount"));
        }
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(EntityDamageByEntityEvent.class)
                .filter(event -> event.getDamager() instanceof Player)
                .handler(event -> {
                    final Player player = (Player) event.getDamager();
                    final ItemStack item = player.getInventory().getItemInMainHand();

                    if (!NBT.readNbt(item).hasTag("wellfed")) {
                        return;
                    }

                    if (!ChanceUtils.parse(this.getChanceFromNBT(item))) {
                        return;
                    }

                    final int enchantLevel = NBT.readNbt(item).getInteger("wellfed");

                    player.setFoodLevel((int) Math.min(player.getFoodLevel(), this.feedAmount.get(enchantLevel)));

                }).bindWith(consumer);
    }
}
