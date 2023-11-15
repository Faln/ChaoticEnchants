package me.faln.chaoticenchants;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import lombok.Getter;
import me.faln.chaoticenchants.commands.CommandHandler;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.enchants.manager.EnchantManager;
import me.faln.chaoticenchants.enchants.registry.EnchantRegistry;
import me.faln.chaoticenchants.items.CleansingWand;
import me.faln.chaoticenchants.items.LuckyGem;
import me.faln.chaoticenchants.lang.LangManager;
import me.faln.chaoticenchants.listeners.ArmorEquipListener;
import me.faln.chaoticenchants.listeners.BookUncoverListener;
import me.faln.chaoticenchants.listeners.EnchantApplyListener;
import me.faln.chaoticenchants.rarity.RarityRegistry;
import me.faln.chaoticenchants.files.impl.FilesRegistry;
import me.faln.chaoticenchants.utils.ArmorListener;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.utils.Players;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@Getter
public final class ChaoticEnchants extends ExtendedJavaPlugin {

    private final FilesRegistry filesRegistry = new FilesRegistry(this);
    private final RarityRegistry rarityRegistry = new RarityRegistry(this);
    private final EnchantRegistry enchantRegistry = new EnchantRegistry(this);
    private final EnchantManager enchantManager = new EnchantManager(this);
    private final LangManager langManager = new LangManager(this);

    private final LuckyGem luckyGem = new LuckyGem(this);
    private final CleansingWand cleansingWand = new CleansingWand();

    @Override
    public void enable() {

        new CommandHandler(this);

        this.bindModule(this.rarityRegistry);
        this.bindModule(this.enchantRegistry);
        this.bindModule(this.langManager);
        this.bindModule(new ArmorListener(new ArrayList<>(0)));
        this.bindModule(new ArmorEquipListener(this));
        this.bindModule(new EnchantApplyListener(this));
        this.bindModule(new BookUncoverListener(this));
        this.bindModule(this.luckyGem);

        Players.forEach(player -> {
            for (final ItemStack armor : player.getInventory().getArmorContents()) {
                this.enchantManager.activateEnchants(armor, player);
            }
        });

    }

    @Override
    public void disable() {
        // Plugin shutdown logic
    }
}
