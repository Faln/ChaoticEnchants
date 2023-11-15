package me.faln.chaoticenchants.items;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.utils.Replacer;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public final class LuckyGem implements TerminableModule {

    private static final String LUCKY_GEM_NBT = "lucky-gem";

    private final ChaoticEnchants plugin;
    private final YMLConfig config;

    private Map<String, Integer> tiers;
    @Getter
    private int cost;
    @Getter
    private ItemStack item;

    public LuckyGem(final ChaoticEnchants plugin) {
        this.plugin = plugin;
        this.config = plugin.getFilesRegistry().get("config");
    }

    @Override
    public void setup(@NotNull final TerminableConsumer consumer) {
        this.tiers = new HashMap<>();

        for (final String tier : this.config.section("lucky-gem.tiers").getKeys(false)) {
            this.tiers.put(tier, this.config.parseInt("lucky-gem.tiers." + tier));
        }

        this.cost = this.config.parseInt("lucky-gem.cost");
        this.item = this.config.getItemstack("lucky-gem").build();

        NBT.modify(this.item, nbt -> {
            nbt.setString(LUCKY_GEM_NBT, "true");
        });

        Events.subscribe(InventoryClickEvent.class)
                .filter(event -> event.getCursor().getType() != Material.AIR)
                .filter(event -> event.getCurrentItem() != null)
                .filter(event -> event.getCurrentItem().getAmount() == 1)
                .filter(event -> event.getCurrentItem().getType() != Material.AIR)
                .filter(event -> NBT.readNbt(event.getCursor()).hasTag(LUCKY_GEM_NBT))
                .filter(event -> NBT.readNbt(event.getCurrentItem()).hasTag("enchant-book"))
                .handler(event -> {
                    final ItemStack item = event.getCurrentItem().clone();
                    final String enchantId = NBT.readNbt(item).getString("enchant-book");

                    if (!this.plugin.getEnchantRegistry().containsKey(enchantId)) {
                        return;
                    }

                    final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().get(enchantId);
                    final int toAdd = this.tiers.containsKey(enchant.getRarity().getId()) ? this.tiers.get(enchant.getRarity().getId()) : 0;

                    if (event.getCursor().getAmount() <= 1) {
                        event.setCursor(null);
                    } else {
                        event.getCursor().setAmount(event.getCursor().getAmount() - 1);
                    }

                    event.setCancelled(true);
                    event.setCurrentItem(this.plugin.getEnchantManager().increaseSuccess(toAdd, item));
                    this.plugin.getLangManager().send(event.getWhoClicked(), "lucky-gem-applied", new Replacer()
                            .add("%enchant%", enchant.getDisplayName().replace("%rarity-color%", enchant.getRarity().getColor()))
                    );

                }).bindWith(consumer);
    }
}
