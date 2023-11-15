package me.faln.chaoticenchants.files.config;

import me.faln.chaoticenchants.files.config.YMLConfig;
import me.lucko.helper.utils.Log;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public final class YMLConfigFactory {

    private final JavaPlugin main;

    public YMLConfigFactory(final JavaPlugin main) {
        this.main = main;
    }

    public YMLConfig createConfig(final File folder, final String name) {

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File ymlFile = new File(folder, name + ".yml");

        if (!ymlFile.exists()) {
            try {
                this.main.saveResource(ymlFile.getName(), false);

                if (ymlFile.createNewFile()) {
                    Log.info("Created File: " + name);
                }
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        final FileConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);

        return new YMLConfig(ymlFile, config);
    }

    public YMLConfig createConfig(final File folder, final String name, final String path) {

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File ymlFile = new File(folder, name + ".yml");

        if (!ymlFile.exists()) {
            try {
                this.main.saveResource(path + File.separator + ymlFile.getName(), false);

                if (ymlFile.createNewFile()) {
                    Log.info("Created file: " + ymlFile.getName());
                }
            } catch (final IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        final FileConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);

        return new YMLConfig(ymlFile, config);
    }

    public YMLConfig createOptionalConfig(final File folder, final String name) {

        if (!folder.exists()) {
            folder.mkdirs();
        }

        final File ymlFile = new File(folder, name + ".yml");
        return new YMLConfig(ymlFile, YamlConfiguration.loadConfiguration(ymlFile));
    }
}
