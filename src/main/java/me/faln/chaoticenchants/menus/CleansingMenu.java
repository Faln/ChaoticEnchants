package me.faln.chaoticenchants.menus;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.Color;
import me.faln.chaoticenchants.utils.Replacer;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CleansingMenu extends Gui {

    private final ChaoticEnchants plugin;
    private final YMLConfig config;
    private final ItemStack itemStack;

    public CleansingMenu(
            final ChaoticEnchants plugin,
            final Player player,
            final ItemStack itemStack,
            final YMLConfig config
    ) {
        super(player, config.parseInt("size"), config.coloredString("title"));
        this.itemStack = itemStack;
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
                .buildConsumer(ClickType.LEFT, event -> {
                    super.getPlayer().getInventory().addItem(this.plugin.getCleansingWand().getItem());
                    this.close();
                })
        );

        final ReadableNBT nbt = NBT.readNbt(this.itemStack);
        final List<String> loreAddon = this.config.coloredList("enchant.lore-addon");
        final List<ItemStack> enchants = this.plugin.getEnchantRegistry().keySet().stream()
                .filter(nbt::hasTag)
                .map(this::toItem)
                .collect(Collectors.toList());
        final List<ItemStack> allEnchants = this.vanillaEnchantsToItem(this.itemStack);

        allEnchants.addAll(enchants);

        final Iterator<ItemStack> iterator = allEnchants.iterator();

        while (iterator.hasNext()) {
            try {
                final int slot = this.getFirstEmpty();
                final ItemStack item = iterator.next();

                item.getLore().addAll(loreAddon);

                this.setItem(slot, Item.builder(item)
                        .bind(ClickType.LEFT, event -> {
                            final Player player = super.getPlayer();

                            final String enchantId = NBT.readNbt(item).getString("enchant-id");
                            final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().get(enchantId);

                            NBT.modify(this.itemStack, n -> {
                                n.removeKey(enchantId);
                            });

                            this.plugin.getEnchantManager().removeLore(this.itemStack, enchant);
                            this.plugin.getLangManager().send(player, "enchant-removed", new Replacer()
                                    .add("%enchant%", item.getItemMeta().getDisplayName())
                            );

                            this.setItem(slot, ItemStackBuilder.of(Material.AIR).buildItem().build());
                            this.close();
                        }).build());
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
    }

    private ItemStack toItem(final String enchantId) {
        final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().get(enchantId);
        final ItemStack item = ItemStackBuilder.of(Material.BOOK)
                .name(Color.colorize(enchant.getDisplayName().replace("%rarity-color%", enchant.getRarity().getColor())))
                .lore(Color.colorize(enchant.getDescription()))
                .build();

        NBT.modify(item, nbt -> {
            nbt.setString("enchant-id", enchantId);
        });

        return item;
    }

    private List<ItemStack> vanillaEnchantsToItem(final ItemStack itemStack) {
        final List<ItemStack> itemStacks = new LinkedList<>();

        for (final Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
            final String name = "&7" + entry.getKey().getKey().examinableName() + " " + entry.getValue();

            itemStacks.add(ItemStackBuilder.of(Material.BOOK)
                    .name(Color.colorize(name))
                    .build()
            );
        }

        return itemStacks;
    }
}
