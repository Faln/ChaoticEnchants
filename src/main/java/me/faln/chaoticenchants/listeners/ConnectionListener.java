package me.faln.chaoticenchants.listeners;

import lombok.AllArgsConstructor;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class ConnectionListener implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> this.plugin.getEnchantManager().activateEnchants(event.getPlayer()))
                .bindWith(consumer);
    }
}
