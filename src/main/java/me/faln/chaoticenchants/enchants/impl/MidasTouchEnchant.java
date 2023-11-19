package me.faln.chaoticenchants.enchants.impl;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import lombok.Value;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class MidasTouchEnchant extends AbstractEnchant {

    private final ListMultimap<Integer, DropsDTO> drops = MultimapBuilder.hashKeys()
            .arrayListValues()
            .build();

    public MidasTouchEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("midastouch"));

        for (final String level : config.section("midastouch.levels").getKeys(false)) {
            final List<DropsDTO> drops = config.list("midastouch.levels." + level + ".drops").stream()
                    .map(DropsDTO::of)
                    .collect(Collectors.toList());
            final int l = Integer.parseInt(level);

            this.drops.putAll(l, drops);
        }
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(BlockBreakEvent.class)
                .filter(event -> NBT.readNbt(event.getPlayer().getInventory().getItemInMainHand()).hasTag("midastouch"))
                .filter(event -> ChanceUtils.parse(this.getChanceFromNBT(event.getPlayer().getInventory().getItemInMainHand())))
                .handler(event -> {
                    final Block block = event.getBlock();
                    final int enchantLevel = NBT.readNbt(event.getPlayer().getInventory().getItemInMainHand()).getInteger("midastouch");
                    final List<DropsDTO> dtos = this.drops.get(enchantLevel - 1);
                    final ItemStack item = dtos.get(ThreadLocalRandom.current().nextInt(1, dtos.size())).getRandom();

                    block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item);

                }).bindWith(consumer);
    }

    @Value
    private static class DropsDTO {
        Material material;
        int min;
        int max;

        public static DropsDTO of(final String s) {
            final String[] split = s.split(";");

            if (split.length != 3) {
                throw new IllegalArgumentException("Midas Touch invalid drops: " + s);
            }

            final Material mat = Material.getMaterial(split[0].toUpperCase());
            final int min = Integer.parseInt(split[1]);
            final int max = Integer.parseInt(split[2]);

            return new DropsDTO(mat, min, max);
        }

        public ItemStack getRandom() {
            final ItemStack item = new ItemStack(this.material);
            item.setAmount(ThreadLocalRandom.current().nextInt(this.min, this.max + 1));
            return item;
        }
    }
}
