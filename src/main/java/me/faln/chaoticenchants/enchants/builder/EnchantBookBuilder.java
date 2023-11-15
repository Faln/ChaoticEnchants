package me.faln.chaoticenchants.enchants.builder;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.rarity.Rarity;
import me.faln.chaoticenchants.utils.Color;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor @Getter
public final class EnchantBookBuilder {

    private final ChaoticEnchant enchant;
    private int level = 1;
    private double success = 0.0;
    private double failure = 0.0;
    private double destroy = 0.0;
    private int amount = 1;

    public EnchantBookBuilder level(final int level) {
        this.level = level;
        return this;
    }

    public EnchantBookBuilder success(final double success) {
        this.success = success;
        return this;
    }

    public EnchantBookBuilder failure(final double failure) {
        this.failure = failure;
        return this;
    }

    public EnchantBookBuilder destroy(final double destroy) {
        this.destroy = destroy;
        return this;
    }

    public EnchantBookBuilder amount(final int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        final Rarity rarity = this.enchant.getRarity();
        final List<String> lore = this.enchant.getDescription();

        lore.addAll(rarity.getIdentifiedLore().stream()
                .map(s -> s.replace("%max-level%", String.valueOf(this.enchant.getMaxLevel())))
                .map(s -> s.replace("%success%", String.valueOf(this.success)))
                .map(s -> s.replace("%failure%", String.valueOf(this.failure)))
                .map(s -> s.replace("%destroy%", String.valueOf(this.destroy)))
                .map(s -> s.replace("%level%", String.valueOf(this.level)))
                .map(s -> s.replace("%applies-to%", this.applicableToString(this.enchant)))
                .collect(Collectors.toList()));

        this.level = Math.min(this.enchant.getMaxLevel(), this.level);

        final ItemStack book = rarity.getIdentifiedItem()
                .clearLore()
                .amount(this.amount)
                .name(Color.colorize(rarity.getIdentifiedName()
                        .replace("%rarity-color%", rarity.getColor())
                        .replace("%max-level%", String.valueOf(this.enchant.getMaxLevel()))
                        .replace("%success%", String.valueOf(this.success))
                        .replace("%failure%", String.valueOf(this.failure))
                        .replace("%destroy%", String.valueOf(this.destroy))
                        .replace("%enchant-name%", this.enchant.getDisplayName().replace("%rarity-color%", rarity.getColor()))))
                .lore(lore)
                .build();

        final int incinerateAmount = (int) (rarity.getIncinerateFactor() * (this.success / 2));

        NBT.modify(book, n -> {
            n.setString("enchant-book", this.enchant.getId());
            n.setDouble("enchant-success", this.success);
            n.setDouble("enchant-failure", this.failure);
            n.setDouble("enchant-destroy", this.destroy);
            n.setInteger("enchant-level", this.level);
            n.setInteger("incinerate-amount", incinerateAmount);
        });

        return book;
    }

    private String applicableToString(final ChaoticEnchant enchant) {
        return enchant.getApplicableTypes().stream()
                .map(Enum::name)
                .map(s -> s.toUpperCase().charAt(0) + s.substring(1).toLowerCase())
                .collect(Collectors.joining("/"));
    }


}
