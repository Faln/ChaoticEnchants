package me.faln.chaoticenchants.listeners;

import me.faln.chaoticenchants.utils.ArmorEquipEvent;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;

import javax.annotation.Nonnull;

public final class ArmorEquipListener implements TerminableModule {

    @Override
    public void setup(@Nonnull final TerminableConsumer consumer) {
        Events.subscribe(ArmorEquipEvent.class)
                .handler(event -> {

                }).bindWith(consumer);
    }
}
