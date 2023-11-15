package me.faln.chaoticenchants.commands;


import cloud.commandframework.CommandTree;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import me.faln.chaoticenchants.ChaoticEnchants;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

public class CommandHandler {

    private final ChaoticEnchants plugin;
    private PaperCommandManager<CommandSender> commandManager;
    private AnnotationParser<CommandSender> annotationParser;

    public CommandHandler(final ChaoticEnchants plugin) {
        this.plugin = plugin;

        final Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction =
                AsynchronousCommandExecutionCoordinator.<CommandSender>builder().build();

        try {
            this.commandManager = new PaperCommandManager<>(
                    plugin,
                    executionCoordinatorFunction,
                    Function.identity(),
                    Function.identity()
            );

            final Function<ParserParameters, CommandMeta> commandMetaFunction = p ->
                    CommandMeta.simple()
                            .with(CommandMeta.DESCRIPTION, p.get(StandardParameters.DESCRIPTION, "No description"))
                            .build();

            this.annotationParser = new AnnotationParser<>(
                    this.commandManager,
                    CommandSender.class,
                    commandMetaFunction
            );

        } catch (Exception e) {
            plugin.getLogger().warning("Failed to initialize command manager");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        this.register();
        this.registerCommands();
    }

    private void register() {
        if (this.commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            this.commandManager.registerBrigadier();
        }

        if (this.commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.commandManager.registerAsynchronousCompletions();
        }
    }

    public void registerAnnotationCommand(final Object object) {
        this.annotationParser.parse(object);
    }

    private void registerCommands() {
        this.annotationParser.parse(new EnchantCommands(this.plugin));
    }


}
