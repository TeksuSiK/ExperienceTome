package pl.teksusik.experiencetome;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.teksusik.experiencetome.deposit.ExperienceDepositListener;
import pl.teksusik.experiencetome.withdraw.ExperienceWithdrawListener;

import java.io.File;

public class ExperienceTomePlugin extends JavaPlugin {
    private ExperienceTomeConfiguration configuration;
    private NamespacedKey key;

    @Override
    public void onEnable() {
        this.configuration = ConfigManager.create(ExperienceTomeConfiguration.class, (it) -> {
            it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true), new SerdesBukkit());
            it.withBindFile(new File(this.getDataFolder(), "config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        this.key = new NamespacedKey(this, "tome");

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ExperienceDepositListener(this.configuration, this.key), this);
        pluginManager.registerEvents(new ExperienceWithdrawListener(this.configuration, this.key), this);

        ExperienceTomeRecipe recipe = new ExperienceTomeRecipe(this.key, this.configuration.getTomeMaterial(), this.configuration.getDisplayName(), this.configuration.getLore(), this.configuration.getMaximumExperience(), this.configuration.getCrafting());
        this.getServer().addRecipe(recipe.toShapedRecipe());
    }

    @Override
    public void onDisable() {
    }
}

