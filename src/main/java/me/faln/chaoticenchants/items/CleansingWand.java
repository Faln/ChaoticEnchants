package me.faln.chaoticenchants.items;

import lombok.Getter;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class CleansingWand implements TerminableModule {

    private final ChaoticEnchants plugin;
    private ItemStack item;

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {

    }
}
