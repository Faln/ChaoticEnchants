package me.faln.chaoticenchants.enchants.registry;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.enchants.impl.*;
import me.faln.chaoticenchants.files.AbstractRegistry;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.rarity.Rarity;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@AllArgsConstructor
public final class EnchantRegistry extends AbstractRegistry<String, ChaoticEnchant> implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {

        final YMLConfig config = this.plugin.getFilesRegistry().get("enchants");

        // COMMON ENCHANTS
        this.put("clarity", new ClarityEnchant(this.plugin, config));
        this.put("decapitation", new DecapitationEnchant(this.plugin, config));
        this.put("firecracker", new FirecrackerEnchant(this.plugin, config));
        this.put("poisondart", new PoisonDartEnchant(this.plugin, config));
        this.put("skystepper", new SkyStepperEnchant(this.plugin, config));
        this.put("snowshoes", new SnowShoesEnchant(this.plugin, config));
        this.put("wellfed", new WellFedEnchant(this.plugin, config));

        // RARE ENCHANTS


        this.values().forEach(enchant -> enchant.bindModuleWith(consumer));
        this.plugin.getLogger().info("Loaded Enchants: " + this.size());
    }

    public Optional<ChaoticEnchant> getRandom(final Rarity rarity) {
        final Set<ChaoticEnchant> enchants = this.values().stream()
                .filter(enchant -> enchant.getRarity().getId().equals(rarity.getId()))
                .collect(Collectors.toSet());

        if (enchants.isEmpty()) {
            throw new IllegalArgumentException("There are no enchants in rarity: " + rarity.getId());
        }

        return enchants.stream()
                .skip(ThreadLocalRandom.current().nextInt(enchants.size()))
                .findAny();
    }
}
