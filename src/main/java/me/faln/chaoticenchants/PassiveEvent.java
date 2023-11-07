package me.faln.chaoticenchants;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface PassiveEvent {

    void handle(final Player player);

}
