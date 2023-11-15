package me.faln.chaoticenchants.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.faln.chaoticenchants.utils.Color;
import me.faln.chaoticenchants.utils.Replacer;
import org.bukkit.command.CommandSender;

import java.util.List;

@AllArgsConstructor @Getter
public final class Message {

    private final List<String> message;

    public void send(final CommandSender sender) {
        Color.colorize(this.message).forEach(sender::sendMessage);
    }

    public void send(final CommandSender sender, final Replacer replacer) {
        Color.colorize(replacer.parse(this.message)).forEach(sender::sendMessage);
    }
}
