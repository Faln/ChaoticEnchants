package me.faln.chaoticenchants.enchants.impl;

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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public final class PoisonDartEnchant extends AbstractEnchant {

    private final Map<Integer, PoisonDartData> dartData = new HashMap<>();

    public PoisonDartEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("poisondart"));

        for (final String s : config.section("poisondart.levels").getKeys(false)) {
            final int level = Integer.parseInt(s);
            final int poisonLevel = config.parseInt("poisondart.levels." + s + ".poison-level");
            final int duration = config.parseInt("poisondart.levels." + s + ".duration");

            this.dartData.put(level, new PoisonDartData(poisonLevel, duration));
        }

    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(ProjectileHitEvent.class)
                .filter(event -> event.getEntity() instanceof Arrow)
                .filter(event -> event.getHitEntity() instanceof Player)
                .filter(event -> event.getEntity().getShooter() instanceof Player)
                .handler(event -> {
                    final Player player = (Player) event.getHitEntity();
                    final Player shooter = (Player) event.getEntity().getShooter();
                    final ItemStack item = shooter.getInventory().getItemInMainHand();

                    if (!NBT.readNbt(item).hasTag("poisondart")) {
                        return;
                    }

                    if (!ChanceUtils.parse(this.getChanceFromNBT(item))) {
                        return;
                    }

                    final int dartLevel = NBT.readNbt(item).getInteger("poisondart");
                    final PoisonDartData data = this.dartData.get(dartLevel - 1);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, data.duration, data.poisonLevel));

                }).bindWith(consumer);
    }

    @Value
    private static class PoisonDartData {
        int poisonLevel;
        int duration;
    }
}
