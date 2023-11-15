package me.faln.chaoticenchants.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.enchants.builder.EnchantBookBuilder;
import me.faln.chaoticenchants.enchants.registry.EnchantRegistry;
import me.faln.chaoticenchants.menus.InfuserMenu;
import me.faln.chaoticenchants.utils.Color;
import me.faln.chaoticenchants.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public final class EnchantCommands {

    private final ChaoticEnchants plugin;
    private final EnchantRegistry enchantRegistry;

    public EnchantCommands(final ChaoticEnchants plugin) {
        this.plugin = plugin;
        this.enchantRegistry = plugin.getEnchantRegistry();
    }

    @CommandMethod("infuser")
    @CommandPermission("chaoticenchants.use")
    @CommandDescription("Opens the infuser menu")
    private void openInfuser(final CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }

        new InfuserMenu(this.plugin, (Player) sender, this.plugin.getFilesRegistry().get("infuser-menu")).open();
    }

    @CommandMethod("infuser giveBook <player> <enchant-id> <level> <success> <failure> <destroy> <amount>")
    @CommandPermission("chaoticenchants.admin")
    @CommandDescription("Gives a enchantment book")
    private void giveBook(
            @NonNull final CommandSender sender,
            @Argument("player") @NonNull final Player player,
            @Argument("enchant-id") @NonNull final String enchantId,
            @Argument("level") final int level,
            @Argument("success") final double success,
            @Argument("failure") final double failure,
            @Argument("destroy") final double destroy,
            @Argument("amount") final int amount
    ) {
        final ChaoticEnchant enchant = this.enchantRegistry.getOpt(enchantId).orElse(null);

        if (enchant == null) {
            sender.sendMessage(Color.colorize("&c&l[!] &cThis is enchant does not exist."));
            return;
        }

        final ItemStack enchantBook = new EnchantBookBuilder(enchant)
                .level(level)
                .success(success)
                .failure(failure)
                .destroy(destroy)
                .amount(amount)
                .build();

        PlayerUtils.giveItem(player, enchantBook);
    }
}
