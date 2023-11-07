package me.faln.chaoticenchants;

import lombok.Getter;
import me.faln.chaoticenchants.enchants.registry.EnchantRegistry;
import me.faln.chaoticenchants.listeners.ArmorEquipListener;
import me.faln.chaoticenchants.registry.FilesRegistry;
import me.lucko.helper.plugin.ExtendedJavaPlugin;

@Getter
public final class ChaoticEnchants extends ExtendedJavaPlugin {

    private final FilesRegistry filesRegistry = new FilesRegistry(this);
    private final EnchantRegistry enchantRegistry = new EnchantRegistry(this);

    @Override
    public void enable() {

        this.bindModule(new ArmorEquipListener());

    }

    @Override
    public void disable() {
        // Plugin shutdown logic
    }
}
