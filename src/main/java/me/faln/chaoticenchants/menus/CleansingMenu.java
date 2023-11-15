package me.faln.chaoticenchants.menus;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.Color;
import me.faln.chaoticenchants.utils.NumberUtils;
import me.faln.chaoticenchants.utils.Replacer;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;

public final class CleansingMenu extends Gui {

    private final ChaoticEnchants plugin;
    private final YMLConfig config;
    private final ItemStack itemStack;
    private final ItemStack wand;

    public CleansingMenu(
            final ChaoticEnchants plugin,
            final Player player,
            final ItemStack itemStack,
            final ItemStack wand,
            final YMLConfig config
    ) {
        super(player, config.parseInt("size"), config.coloredString("title"));
        this.itemStack = itemStack;
        this.wand = wand;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public void redraw() {
        this.setItems(this.config.intList("filler.slots"), this.config.getItemstack("filler")
                .buildItem()
                .build()
        );

        this.setItem(this.config.parseInt("cancel.slot"), this.config.getItemstack("cancel")
                .buildConsumer(ClickType.LEFT, event -> this.close())
        );

        final ReadableNBT nbt = NBT.readNbt(this.itemStack);
        final List<String> loreAddon = this.config.coloredList("enchant.lore-addon");
        final Iterator<Item> iterator = this.plugin.getEnchantRegistry().keySet().stream()
                .filter(nbt::hasTag)
                .map(this::toItem)
                .iterator();

        while (iterator.hasNext()) {
            try {
                final int slot = this.getFirstEmpty();
                final ItemStack item = iterator.next().getItemStack();

                item.getLore().addAll(loreAddon);

                this.setItem(slot, Item.builder(item).build());
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
    }

    private Item toItem(final String enchantId) {
        final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().get(enchantId);
        return ItemStackBuilder.of(Material.BOOK)
                .name(enchant.getDisplayName().replace("%rarity-color%", enchant.getRarity().getColor()))
                .lore(Color.colorize(enchant.getDescription()))
                .buildConsumer(ClickType.LEFT, event -> {
                    final Player player = super.getPlayer();
                    final ItemStack wand = this.plugin.getCleansingWand().getItem();

                    if (!player.getInventory().contains(wand)) {
                        return;
                    }

                    player.getInventory().remove(wand);

                    NBT.modify(this.itemStack, nbt -> {
                        nbt.removeKey(enchantId);
                    });

                    this.plugin.getEnchantManager().removeLore(this.itemStack, enchant);
                    this.plugin.getLangManager().send(player, "enchant-removed", new Replacer()
                            .add("%enchant%", this.itemStack.getItemMeta().getDisplayName())
                    );
                });
    }
}
