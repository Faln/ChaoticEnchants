package me.faln.chaoticenchants.enchants.impl;

import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ExtraPaddingEnchant extends AbstractEnchant {

    public ExtraPaddingEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("extrapadding"));
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {}

    @Override
    public void activate(final Player player, final int level) {
        super.activate(player, level);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, PotionEffect.INFINITE_DURATION, 2));
    }

    @Override
    public void deactivate(final Player player) {
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
    }
}
