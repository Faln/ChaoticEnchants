package me.faln.chaoticenchants.menus;

import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.NumberUtils;
import me.faln.chaoticenchants.utils.Replacer;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AmountSelectorMenu extends Gui {

    private final ChaoticEnchants plugin;
    private final YMLConfig config;
    private final ItemStack item;
    private final int pricePer;

    private int amount = 1;


    public AmountSelectorMenu(
            final ChaoticEnchants plugin,
            final Player player,
            final ItemStack item,
            final int pricePer,
            final YMLConfig config
    ) {
        super(player, config.parseInt("size"), config.coloredString("title"));
        this.plugin = plugin;
        this.config = config;
        this.item = item;
        this.pricePer = pricePer;
    }

    @Override
    public void redraw() {
        this.setItem(this.config.parseInt("item.slot"), Item.builder(this.item).build());

        this.setItems(this.config.intList("filler.slots"), this.config.getItemstack("filler")
                .buildItem()
                .build()
        );

        this.setItem(this.config.parseInt("confirm.slot"), ItemStackBuilder.of(this.config.material("confirm.material"))
                .name(this.config.coloredString("confirm.name")
                        .replace("%amount%", String.valueOf(this.amount))
                        .replace("%item%", this.item.getItemMeta().getDisplayName())
                        .replace("%exp%", NumberUtils.formatExp(this.pricePer * this.amount)))
                .lore(this.config.coloredList("confirm.lore"))
                .model(this.config.parseInt("confirm.custom-model-data"))
                .buildConsumer(ClickType.LEFT, event -> {
                    final Player player = super.getPlayer();

                    if (player.getInventory().firstEmpty() == -1) {
                        this.plugin.getLangManager().send(player, "inventory-full");
                        return;
                    }

                    final int cost = this.pricePer * this.amount;

                    if (player.getTotalExperience() < cost) {
                        this.plugin.getLangManager().send(player, "not-enough-exp");
                        return;
                    }

                    player.setTotalExperience(player.getTotalExperience() - cost);
                    player.getInventory().addItem(this.item);
                    this.plugin.getLangManager().send(player, "item-purchase", new Replacer()
                            .add("%amount%", this.amount)
                            .add("%item%", this.item.getItemMeta().getDisplayName())
                            .add("%exp%", NumberUtils.formatExp(cost))
                    );
                    this.item.setAmount(1);
                })
        );

        for (final String s : this.config.section("add").getKeys(false)) {
            final String path = "add." + s + ".";
            final int add = this.config.parseInt(path + "add");
            final int slot = Integer.parseInt(s);

            if (this.amount + add > 64) {
                this.setItem(slot, ItemStackBuilder.of(Material.BLACK_STAINED_GLASS_PANE)
                        .buildItem()
                        .build());
                continue;
            }

            this.setItem(slot, ItemStackBuilder.of(this.config.material(path + "material"))
                    .name(this.config.coloredString(path + "name"))
                    .lore(this.config.coloredList(path + "lore"))
                    .model(this.config.parseInt(path + "custom-model-data"))
                    .amount(this.config.parseInt(path + "amount"))
                    .buildConsumer(ClickType.LEFT, event -> {
                        this.amount += add;
                        this.item.setAmount(this.amount);
                        this.redraw();
                    })
            );
        }

        for (final String s : this.config.section("remove").getKeys(false)) {
            final String path = "remove." + s + ".";
            final int remove = this.config.parseInt(path + "remove");
            final int slot = Integer.parseInt(s);

            if (this.amount - remove < 1) {
                this.setItem(slot, ItemStackBuilder.of(Material.BLACK_STAINED_GLASS_PANE)
                        .buildItem()
                        .build());
                continue;
            }

            this.setItem(slot, ItemStackBuilder.of(this.config.material(path + "material"))
                    .name(this.config.coloredString(path + "name"))
                    .lore(this.config.coloredList(path + "lore"))
                    .model(this.config.parseInt(path + "custom-model-data"))
                    .amount(this.config.parseInt(path + "amount"))
                    .buildConsumer(ClickType.LEFT, event -> {
                        this.amount -= remove;
                        this.item.setAmount(this.amount);
                        this.redraw();
                    })
            );
        }

    }
}
