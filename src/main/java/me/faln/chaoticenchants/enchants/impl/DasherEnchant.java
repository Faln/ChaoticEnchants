package me.faln.chaoticenchants.enchants.impl;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.ChanceUtils;
import me.faln.chaoticenchants.utils.CooldownMap;
import me.lucko.helper.Events;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.terminable.TerminableConsumer;
import org.bukkit.entity.Player;

public final class DasherEnchant extends AbstractEnchant {

    private final CooldownMap cooldownMap = new CooldownMap(15);

    public DasherEnchant(
            final ChaoticEnchants plugin,
            final YMLConfig config
    ) {
        super(plugin, config.section("dasher"));
        this.plugin.bindModule(this.cooldownMap);
    }

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {
        Events.subscribe(PlayerJumpEvent.class)
                .filter(event -> event.getPlayer().isSneaking())
                .filter(event -> Metadata.provideForPlayer(event.getPlayer()).has(this.metadataKey))
                .filter(event -> !this.cooldownMap.containsKey(event.getPlayer().getUniqueId()))
                .filter(event -> ChanceUtils.parse(this.getChanceFromLevel(event.getPlayer())))
                .handler(event -> {
                    final Player player = event.getPlayer();
                    final int enchantLevel = Metadata.provideForPlayer(player)
                            .get(this.metadataKey)
                            .orElseThrow(IllegalStateException::new);

                    player.setVelocity(player.getLocation().clone().getDirection().multiply(enchantLevel).setY(enchantLevel));
                    this.cooldownMap.put(player);
                }).bindWith(consumer);
    }
}
