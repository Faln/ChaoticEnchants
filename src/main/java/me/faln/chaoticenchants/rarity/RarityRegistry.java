package me.faln.chaoticenchants.rarity;

import lombok.AllArgsConstructor;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.registry.AbstractRegistry;
import me.faln.chaoticenchants.registry.YMLConfig;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;

import javax.annotation.Nonnull;

@AllArgsConstructor
public final class RarityRegistry extends AbstractRegistry<String, Rarity> implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@Nonnull final TerminableConsumer consumer) {
        final YMLConfig config = this.plugin.getFilesRegistry().get("rarity");

        for (final String rarityId : config.section("").getKeys(false)) {

        }
    }

}
