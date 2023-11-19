package me.faln.chaoticenchants.hooks;

import me.faln.chaoticenchants.ChaoticEnchants;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.inventory.ItemStack;

public final class ShopGUIHook {

    private final ChaoticEnchants plugin;

    public ShopGUIHook(final ChaoticEnchants plugin) {
        this.plugin = plugin;
    }

    public boolean isLoaded() {
        return ShopGuiPlusApi.getPlugin().getShopManager().areShopsLoaded();
    }

    public double getPrice(final ItemStack itemStack) {
        return ShopGuiPlusApi.getItemStackPriceSell(itemStack);
    }
}
