package me.faln.chaoticenchants.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.files.AbstractRegistry;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.Replacer;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class LangManager extends AbstractRegistry<String, Message> implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@NotNull final TerminableConsumer consumer) {
        final YMLConfig config = this.plugin.getFilesRegistry().get("lang");

        for (final String key : config.section("").getKeys(false)) {
            this.put(key, new Message(config.list(key)));
        }
    }

    public void send(final CommandSender sender, final String message) {
        this.get(message).send(sender);
    }

    public void send(final CommandSender sender, final String message, final Replacer replacer) {
        this.get(message).send(sender, replacer);
    }
}
