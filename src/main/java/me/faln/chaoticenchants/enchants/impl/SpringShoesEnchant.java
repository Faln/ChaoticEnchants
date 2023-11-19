package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class SpringShoesEnchant extends AbstractEnchant {

    public SpringShoesEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("springshoes"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {}

    @Override
    public void activate(final Player player, final int level) {
        super.activate(player, level);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, level));
    }

    @Override
    public void deactivate(final Player player) {
        player.removePotionEffect(PotionEffectType.JUMP);
    }
}
