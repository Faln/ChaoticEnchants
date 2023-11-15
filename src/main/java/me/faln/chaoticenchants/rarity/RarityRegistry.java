package me.faln.chaoticenchants.rarity;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.files.AbstractRegistry;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;

@AllArgsConstructor
public final class RarityRegistry extends AbstractRegistry<String, Rarity> implements TerminableModule {

    private final ChaoticEnchants plugin;

    @Override
    public void setup(@NonNull final TerminableConsumer consumer) {

        final YMLConfig config = this.plugin.getFilesRegistry().get("rarity");

        for (final String rarityId : config.section("").getKeys(false)) {
            final Rarity rarity = Rarity.builder()
                    .id(rarityId)
                    .cost(config.parseInt(rarityId + ".cost"))
                    .color(config.string(rarityId + ".color"))
                    .incinerateFactor(config.parseDouble(rarityId + ".incinerate-factor"))
                    .displayName(config.coloredString(rarityId + ".display"))
                    .identifiedItem(ItemStackBuilder.of(config.material(rarityId + ".identified.material"))
                            .name(config.string(rarityId + ".identified.name"))
                            .lore(config.list(rarityId + ".identified.lore"))
                            .model(config.parseInt(rarityId + ".identified.custom-model-data", 0)))
                    .identifiedName(config.string(rarityId + ".identified.name"))
                    .identifiedLore(config.list(rarityId + ".identified.lore"))
                    .unidentifiedItem(ItemStackBuilder.of(config.material(rarityId + ".unidentified.material"))
                            .name(config.coloredString(rarityId + ".unidentified.name"))
                            .lore(config.coloredList(rarityId + ".unidentified.lore"))
                            .model(config.parseInt(rarityId + ".unidentified.custom-model-data", 0)))
                    .unidentifiedLore(config.coloredList(rarityId + ".unidentified.lore"))
                    .build();

            this.put(rarityId, rarity);
        }

        this.plugin.getLogger().info("Loaded Rarities: " + this.size());
    }

}
