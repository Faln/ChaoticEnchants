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
        this.put("autosmelt", new AutoSmeltEnchant(this.plugin, config));
        this.put("gills", new GillsEnchant(this.plugin, config));
        this.put("midastouch", new MidasTouchEnchant(this.plugin, config));
        this.put("springshoes", new SpringShoesEnchant(this.plugin, config));
        this.put("arrowstorm", new ArrowStormEnchant(this.plugin, config));
        this.put("dasher", new DasherEnchant(this.plugin, config));

        // LEGENDARY ENCHANTS
        this.put("carefulstep", new CarefulStepEnchant(this.plugin, config));
        this.put("fireforged", new FireForgedEnchant(this.plugin, config));
        this.put("gardener", new GardenerEnchant(this.plugin, config));
        this.put("grind", new GrindEnchant(this.plugin, config));
        this.put("mealplanning", new MealPlanningEnchant(this.plugin, config));
        this.put("rocketboots", new RocketBootsEnchant(this.plugin, config));
        this.put("runicobstruction", new RunicObstructionEnchant(this.plugin, config));

        // MYTHICAL ENCHANTS
        this.put("echolocation", new EcholocationEnchant(this.plugin, config));
        this.put("excavator", new ExcavatorEnchant(this.plugin, config));
        this.put("extrapadding", new ExtraPaddingEnchant(this.plugin, config));
        this.put("greedygreens", new GreedyGreensEnchant(this.plugin, config));
        this.put("greenhouse", new GreenHouseEnchant(this.plugin, config));
        this.put("herbalist", new HerbalistEnchant(this.plugin, config));
        this.put("deathgrip", new DeathGripEnchant(this.plugin, config));
        this.put("ironlungs", new IronLungsEnchant(this.plugin, config));
        this.put("lifeforce", new LifeforceEnchant(this.plugin, config));
        this.put("refurbish", new RefurbishEnchant(this.plugin, config));
        this.put("enchantmentdetector", new EnchantmentDetectorEnchant(this.plugin, config));
        this.put("telekinesis", new TelekinesisEnchant(this.plugin, config));

        this.values().forEach(enchant -> enchant.bindModuleWith(consumer));
        this.plugin.getLogger().info("Loaded Enchants: " + this.size());
    }

    public Optional<ChaoticEnchant> getRandom(final Rarity rarity) {
        final Set<ChaoticEnchant> enchants = this.values().stream()
                .filter(ChaoticEnchant::isEnabled)
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
