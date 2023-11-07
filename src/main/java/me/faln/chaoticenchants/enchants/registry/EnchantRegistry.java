package me.faln.chaoticenchants.enchants.registry;

import lombok.AllArgsConstructor;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.AbstractEnchant;
import me.faln.chaoticenchants.enchants.impl.ClarityEnchant;
import me.faln.chaoticenchants.registry.AbstractRegistry;
import me.faln.chaoticenchants.registry.YMLConfig;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;

import javax.annotation.Nonnull;

@AllArgsConstructor
public final class EnchantRegistry extends AbstractRegistry<String, AbstractEnchant> implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@Nonnull final TerminableConsumer consumer) {
        consumer.bindModule(this);

        final YMLConfig config = this.plugin.getFilesRegistry().get("enchants");

        this.put("clarity", new ClarityEnchant(this.plugin, config));


        this.values().forEach(enchant -> enchant.bindModuleWith(consumer));
    }
}
