package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.rarity.Rarity;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class EnchantmentDetectorEnchant extends AbstractEnchant {

    private final Map<Integer, Rarity> drops = new HashMap<>();

    public EnchantmentDetectorEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("enchantmentdetector"));

        for (final String s : config.section("enchantmentdetector.levels").getKeys(false)) {
            final String rarity = config.string("enchantmentdetector.levels." + s + ".rarity");

            this.drops.put(Integer.parseInt(s), plugin.getRarityRegistry().get(rarity));
        }

    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(BlockBreakEvent.class)
                .filter(event -> event.getBlock().getType().name().endsWith("_ORE"))
                .filter(event -> NBT.readNbt(event.getPlayer().getInventory().getItemInMainHand()).hasTag("enchantmentdetector"))
                .filter(event -> ChanceUtils.parse(this.getChanceFromNBT(event.getPlayer().getInventory().getItemInMainHand())))
                .handler(event -> {
                    final Player player = event.getPlayer();
                    final int enchantLevel = NBT.readNbt(player.getInventory().getItemInMainHand()).getInteger("enchantmentdetector");
                    final ItemStack item = this.plugin.getEnchantManager().getUnidentifiedEnchant(this.drops.get(enchantLevel - 1));

                    player.getInventory().addItem(item);
                }).bindWith(consumer);
    }
}
