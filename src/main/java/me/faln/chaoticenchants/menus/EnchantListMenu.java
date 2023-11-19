package me.faln.chaoticenchants.menus;

import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.utils.Color;
import me.lucko.helper.item.ItemStackBuilder;
import me.lucko.helper.menu.Gui;
import me.lucko.helper.menu.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public final class EnchantListMenu extends Gui {

    private final ChaoticEnchants plugin;
    private final List<ChaoticEnchant> enchants;

    public EnchantListMenu(
            final ChaoticEnchants plugin,
            final Player player,
            final int size,
            final List<ChaoticEnchant> enchants
    ) {
        super(player, size, Color.colorize("&8Enchants"));
        this.plugin = plugin;
        this.enchants = enchants;
    }

    @Override
    public void redraw() {
        if (this.isFirstDraw()) {
            final Iterator<ItemStack> iterator = this.enchants.stream()
                    .map(this::toItemstack)
                    .collect(Collectors.toList())
                    .iterator();

            while (iterator.hasNext()) {
                try {
                    final int slot = this.getFirstEmpty();
                    final ItemStack item = iterator.next();

                    this.setItem(slot, Item.builder(item).build());
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
    }

    private ItemStack toItemstack(final ChaoticEnchant enchant) {

        final String color = enchant.getRarity().getColor();
        final List<String> addon = Arrays.asList(
                "",
                color + "Applies-To: &f" + this.applicableToString(enchant),
                color + "Max Level: &f" + enchant.getMaxLevel()
        );

        return ItemStackBuilder.of(Material.BOOK)
                .name(Color.colorize(enchant.getDisplayName().replace("%rarity-color%", enchant.getRarity().getColor())))
                .lore(Color.colorize(enchant.getDescription()))
                .lore(Color.colorize(addon))
                .build();
    }

    private String applicableToString(final ChaoticEnchant enchant) {
        return enchant.getApplicableTypes().stream()
                .map(Enum::name)
                .map(s -> s.toUpperCase().charAt(0) + s.substring(1).toLowerCase())
                .collect(Collectors.joining("/"));
    }
}
