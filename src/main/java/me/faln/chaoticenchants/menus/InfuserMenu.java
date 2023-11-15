package me.faln.chaoticenchants.menus;

import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.items.LuckyGem;
import me.faln.chaoticenchants.rarity.Rarity;
import me.faln.chaoticenchants.utils.NumberUtils;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public final class InfuserMenu extends Gui {

    private final ChaoticEnchants plugin;
    private final YMLConfig config;

    public InfuserMenu(final ChaoticEnchants plugin, final Player player, final YMLConfig config) {
        super(player, config.parseInt("size"), config.coloredString("title"));
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public void redraw() {

        this.setItems(this.config.intList("filler.slots"), this.config.getItemstack("filler").buildItem().build());

        this.setItem(this.config.parseInt("lucky-gem.slot"), this.config.getItemstack("lucky-gem")
                .buildConsumer(ClickType.LEFT, event -> {
                    final Player player = super.getPlayer();
                    final LuckyGem luckyGem = this.plugin.getLuckyGem();

                    new AmountSelectorMenu(
                            this.plugin,
                            player,
                            luckyGem.getItem(),
                            luckyGem.getCost(),
                            this.plugin.getFilesRegistry().get("amount-selector-menu")
                    ).open();
                })
        );

        this.setItem(this.config.parseInt("incinerator.slot"), ItemStackBuilder.of(this.config.material("incinerator.material"))
                .name(this.config.coloredString("incinerator.name"))
                .lore(this.config.coloredList("incinerator.lore"))
                .model(this.config.parseInt("incinerator.custom-model-data"))
                .buildConsumer(ClickType.LEFT, event -> {
                    new IncineratorMenu(this.plugin, super.getPlayer(), this.plugin.getFilesRegistry().get("incinerator-menu")).open();
                })
        );

        for (final String key : this.config.section("enchants").getKeys(false)) {
            if (!this.plugin.getRarityRegistry().containsKey(key)) {
                return;
            }

            final Rarity rarity = this.plugin.getRarityRegistry().get(key);
            final String path = "enchants." + key + ".";

            this.setItem(this.config.parseInt(path + "slot"), ItemStackBuilder.of(this.config.material(path + "material"))
                    .name(this.config.coloredString(path + "name"))
                    .model(this.config.parseInt(path + "custom-model-data"))
                    .lore(this.config.list(path + ".lore").stream()
                            .map(s -> s.replace("%cost%", NumberUtils.formatExp(rarity.getCost())))
                            .map(s -> s.replace("%exp%", NumberUtils.formatExp(super.getPlayer())))
                            .collect(Collectors.toList())
                    ).buildConsumer(event -> {
                        this.redraw();
                    }, event -> {
                        final Player player = super.getPlayer();
                        final int cost = rarity.getCost();
                        final ItemStack item = this.plugin.getEnchantManager().getUnidentifiedEnchant(rarity);

                        new AmountSelectorMenu(
                                this.plugin,
                                player,
                                item,
                                cost,
                                this.plugin.getFilesRegistry().get("amount-selector-menu")
                        ).open();
                    })
            );
        }
    }
}
