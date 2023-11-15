package me.faln.chaoticenchants.menus;

import de.tr7zw.changeme.nbtapi.NBT;
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

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public final class IncineratorMenu extends Gui {

    private final ChaoticEnchants plugin;
    private final YMLConfig config;


    public IncineratorMenu(final ChaoticEnchants plugin, final Player player, final YMLConfig config) {
        super(player, config.parseInt("size"), config.coloredString("title"));
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public void redraw() {
        final Player player = super.getPlayer();

        if (this.isFirstDraw()) {
            this.setItems(this.config.intList("filler.slot"), ItemStackBuilder.of(this.config.material("filler.material"))
                    .name(this.config.coloredString("filler.name"))
                    .lore(this.config.coloredList("filler.lore"))
                    .model(this.config.parseInt("filler.custom-model-data"))
                    .buildItem()
                    .build());

            this.setItems(this.config.intList("go-back.slot"), ItemStackBuilder.of(this.config.material("go-back.material"))
                    .name(this.config.coloredString("go-back.name"))
                    .lore(this.config.coloredList("go-back.lore"))
                    .model(this.config.parseInt("go-back.custom-model-data"))
                    .buildConsumer(ClickType.LEFT, event -> {
                        new InfuserMenu(this.plugin, player, this.plugin.getFilesRegistry().get("infuser-menu")).open();
                    }));

            this.setItems(this.config.intList("info.slot"), ItemStackBuilder.of(this.config.material("info.material"))
                    .name(this.config.coloredString("info.name"))
                    .lore(this.config.coloredList("info.lore"))
                    .model(this.config.parseInt("info.custom-model-data"))
                    .buildItem()
                    .build());
        }

        final Set<ItemStack> runes = Arrays.stream(player.getInventory().getContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> NBT.readNbt(itemStack).hasTag("incinerate-amount"))
                .collect(Collectors.toSet());
        final int totalXP = runes.stream()
                .mapToInt(item -> NBT.readNbt(item).getInteger("incinerate-amount") * item.getAmount())
                .sum();

        this.setItems(this.config.intList("incinerate-all.slot"), ItemStackBuilder.of(this.config.material("incinerate-all.material"))
                .name(this.config.coloredString("incinerate-all.name"))
                .lore(this.config.coloredList("incinerate-all.lore").stream()
                        .map(s -> s.replace("%exp%", String.valueOf(totalXP)))
                        .collect(Collectors.toList()))
                .model(this.config.parseInt("incinerate-all.custom-model-data"))
                .buildConsumer(ClickType.LEFT, event -> {

                    if (totalXP == 0) {
                        return;
                    }

                    player.setTotalExperience(player.getTotalExperience() + totalXP);
                    this.plugin.getLangManager().send(player, "incinerate-all", new Replacer()
                            .add("%exp%", NumberUtils.formatExp(totalXP))
                    );

                    runes.forEach(itemStack -> player.getInventory().remove(itemStack));

                    this.close();
                }));

        final Iterator<ItemStack> iterator = runes.iterator();
        final List<String> loreAddon = this.config.coloredList("not-empty-slot.lore-addon");

        while (iterator.hasNext()) {
            try {
                final int slot = this.getFirstEmpty();
                final ItemStack item = iterator.next();

                item.getLore().addAll(loreAddon);

                this.setItem(slot, Item.builder(item)
                        .bind(ClickType.LEFT, event -> {

                            final int exp = NBT.readNbt(item).getInteger("incinerate-amount") * item.getAmount();
                            final Player p = super.getPlayer();

                            p.getInventory().remove(item);
                            p.setTotalExperience(p.getTotalExperience() + exp);
                            this.plugin.getLangManager().send(p, "incinerate-rune", new Replacer()
                                    .add("%exp%", NumberUtils.formatExp(exp))
                                    .add("%rune%", item.getItemMeta().getDisplayName())
                            );

                            this.setItem(slot, ItemStackBuilder.of(Material.AIR)
                                    .buildItem()
                                    .build());
                        }).build()
                );
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }


    }
}
