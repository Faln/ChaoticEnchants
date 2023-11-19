package me.faln.chaoticenchants.items;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.menus.CleansingMenu;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter @RequiredArgsConstructor
public class CleansingWand implements TerminableModule {

    private static final String CLEANSING_WAND_NBT = "cleansing-wand";

    private final ChaoticEnchants plugin;
    private ItemStack item;
    private int cost;

    @Override
    public void setup(@NotNull final TerminableConsumer consumer) {
        final YMLConfig config = this.plugin.getFilesRegistry().get("config");

        this.item = config.getItemstack("cleansing-wand").build();
        this.cost = config.parseInt("cleansing-wand.cost");

        NBT.modify(this.item, nbt -> {
            nbt.setString(CLEANSING_WAND_NBT, "true");
        });

        Events.subscribe(InventoryClickEvent.class)
                .filter(event -> event.getCursor().getType() != Material.AIR)
                .filter(event -> event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)
                .filter(event -> NBT.readNbt(event.getCursor()).hasTag(CLEANSING_WAND_NBT))
                .filter(this::hasEnchants)
                .handler(event -> {
                    event.setCancelled(true);

                    event.getCursor().setAmount(event.getCursor().getAmount() - 1);

                    new CleansingMenu(
                            this.plugin,
                            (Player) event.getWhoClicked(),
                            event.getCurrentItem(),
                            this.plugin.getFilesRegistry().get("cleansing-menu")
                    ).open();
                }).bindWith(consumer);
    }

    private boolean hasEnchants(final InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();

        if (item == null || item.getItemMeta() == null) {
            return false;
        }

        if (item.getItemMeta().hasEnchants()) {
            return true;
        }

        final ReadableNBT nbt = NBT.readNbt(item);

        for (final String enchantId : this.plugin.getEnchantRegistry().keySet()) {
            if (nbt.hasTag(enchantId)) {
                return true;
            }
        }

        return false;
    }
}
