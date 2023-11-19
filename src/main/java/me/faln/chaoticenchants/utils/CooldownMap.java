package me.faln.chaoticenchants.utils;

import me.faln.chaoticenchants.files.AbstractRegistry;
import me.lucko.helper.Schedulers;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class CooldownMap extends AbstractRegistry<UUID, Long> implements TerminableModule {

    private final long cooldown;

    public CooldownMap(final int cooldown) {
        this.cooldown = cooldown * 1000L;
    }

    public void put(final Player player) {
        this.put(player.getUniqueId(), System.currentTimeMillis() + this.cooldown);
    }

    @Override
    public void setup(@NotNull final TerminableConsumer consumer) {
        Schedulers.builder()
                .sync()
                .every(1, TimeUnit.SECONDS)
                .run(() -> this.entrySet().removeIf(entry -> entry.getValue() > System.currentTimeMillis()))
                .bindWith(consumer);
    }
}
