package me.faln.chaoticenchants.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import lombok.NonNull;
import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.enchants.ChaoticEnchant;
import me.faln.chaoticenchants.enchants.builder.EnchantBookBuilder;
import me.faln.chaoticenchants.enchants.registry.EnchantRegistry;
import me.faln.chaoticenchants.menus.InfuserMenu;
import me.faln.chaoticenchants.utils.Color;
import me.faln.chaoticenchants.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @CommandMethod("infuser unenchant <enchant-id>")
    @CommandPermission("chaoticenchants.admin")
    @CommandDescription("Removes an enchant in hand")
    private void enchant(
            @NonNull final CommandSender sender,
            @Argument(value = "enchant-id", suggestions = "enchants") @NonNull final String enchantId
    ) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.colorize("&c&l[!] &cPlayer only"));
            return;
        }

        final Player player = (Player) sender;
        final ItemStack item = player.getInventory().getItemInMainHand();
        final ReadableNBT nbt = NBT.readNbt(item);

        if (!nbt.hasTag(enchantId)) {
            sender.sendMessage(Color.colorize("&c&l[!] &cEnchant not found"));
            return;
        }

        final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().get(enchantId);

        this.plugin.getEnchantManager().removeEnchant(item, enchant);

    }

    @CommandMethod("infuser enchant <enchant-id> <level>")
    @CommandPermission("chaoticenchants.admin")
    @CommandDescription("Enchants a item with provided enchant and level")
    private void enchant(
            @NonNull final CommandSender sender,
            @Argument(value = "enchant-id", suggestions = "enchants") @NonNull final String enchantId,
            @Argument("level") final int level
    ) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Color.colorize("&c&l[!] &cPlayer only"));
            return;
        }

        final Player player = (Player) sender;
        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType() == Material.AIR) {
            player.sendMessage(Color.colorize("&c&l[!] &cCannot enchant AIR"));
            return;
        }

        final ChaoticEnchant enchant = this.plugin.getEnchantRegistry().get(enchantId);

        if (enchant == null) {
            player.sendMessage(Color.colorize("&c&l[!] &cUnknown enchant"));
            return;
        }

        if (level > enchant.getMaxLevel()) {
            player.sendMessage(Color.colorize("&c&l[!] &cUnknown level. Max is: " + enchant.getMaxLevel()));
            return;
        }

        this.plugin.getEnchantManager().applyEnchant(itemStack, enchant, level);

    }

    @CommandMethod("infuser giveUnidentifiedBook <player> <rarity> <amount>")
    @CommandPermission("chaoticenchants.admin")
    @CommandDescription("Gives unidentified enchantment book")
    private void giveUnidentifiedBook(
            @NonNull final CommandSender sender,
            @Argument("player") @NonNull final Player player,
            @Argument(value = "rarity", suggestions = "rarity") @NonNull final String rarityId,
            @Argument("amount") final int amount
    ) {
        
    }

    @CommandMethod("infuser giveBook <player> <enchant-id> <level> <success> <failure> <destroy> <amount>")
    @CommandPermission("chaoticenchants.admin")
    @CommandDescription("Gives enchantment book")
    private void giveBook(
            @NonNull final CommandSender sender,
            @Argument("player") @NonNull final Player player,
            @Argument(value = "enchant-id", suggestions = "enchants") @NonNull final String enchantId,
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

    @Suggestions("enchants")
    public List<String> enchantSuggestions(final CommandContext<CommandSender> sender, final String input) {
        return new ArrayList<>(this.plugin.getEnchantRegistry().keySet());
    }

    @Suggestions("rarity")
    public List<String> raritySuggestions(final CommandContext<CommandSender> sender, final String input) {
        return new ArrayList<>(this.plugin.getRarityRegistry().keySet());
    }
}
