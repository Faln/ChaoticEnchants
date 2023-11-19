package me.faln.chaoticenchants.files.impl;

import me.faln.chaoticenchants.ChaoticEnchants;
import me.faln.chaoticenchants.files.AbstractRegistry;
import me.faln.chaoticenchants.files.config.YMLConfig;
import me.faln.chaoticenchants.files.config.YMLConfigFactory;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;

public class FilesRegistry extends AbstractRegistry<String, YMLConfig> {

    private final ChaoticEnchants plugin;
    private final YMLConfigFactory configFactory;

    public FilesRegistry(final ChaoticEnchants plugin) {
        this.plugin = plugin;
        this.configFactory = new YMLConfigFactory(plugin);

        Stream.of(
                "enchants",
                "rarity",
                "lang",
                "config",
                "infuser-menu",
                "incinerator-menu",
                "amount-selector-menu",
                "cleansing-menu"
        ).forEach(this::createFile);
    }

    public FilesRegistry createFile(final String file) {
        this.put(file, this.configFactory.createConfig(this.plugin.getDataFolder(), file));
        return this;
    }

    public FilesRegistry createFile(final String... files) {
        Arrays.stream(files).forEach(this::createFile);
        return this;
    }

    public FilesRegistry createFile(final File folder, final String... files) {
        String folderName = folder.getAbsolutePath().replaceAll(this.plugin.getDataFolder().getAbsolutePath() + File.separator, "");
        Arrays.stream(files).forEach(fileName -> this.put(fileName, this.configFactory.createConfig(folder, fileName, folderName)));
        return this;
    }

    public FilesRegistry createOptionalFile(final File folder, final String... files) {
        Arrays.stream(files).forEach(fileName -> this.put(fileName, this.configFactory.createOptionalConfig(folder, fileName)));
        return this;
    }

}
