package me.faln.chaoticenchants.enchants.impl;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.NonNull;
import lombok.Value;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class GrindEnchant extends AbstractEnchant {

    private final Map<Integer, ExperienceDTO> map = new HashMap<>();

    public GrindEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("grind"));

        for (final String s : config.section("grind.levels").getKeys(false)) {
            final String string = config.string("grind.levels." + s + ".exp");
            final String[] split = string.split("-");

            this.map.put(Integer.parseInt(s), new ExperienceDTO(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
        }
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(EntityDeathEvent.class)
                .filter(event -> event.getEntity().getKiller() != null)
                .handler(event -> {
                    final Player player = event.getEntity().getKiller();
                    final ItemStack item = player.getInventory().getItemInMainHand();

                    if (!NBT.readNbt(item).hasTag("grind")) {
                        return;
                    }

                    if (!ChanceUtils.parse(this.getChanceFromNBT(item))) {
                        return;
                    }

                    final int enchantLevel = NBT.readNbt(item).getInteger("grind");

                    event.setDroppedExp(event.getDroppedExp() + this.map.get(enchantLevel).get());
                }).bindWith(consumer);
    }

    @Value
    private static class ExperienceDTO {
        int min;
        int max;

        public int get() {
            return ThreadLocalRandom.current().nextInt(min, max + 1);
        }
    }
}
