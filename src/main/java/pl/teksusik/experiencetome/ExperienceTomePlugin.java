package pl.teksusik.experiencetome;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.i18n.configs.LocaleConfigManager;
import eu.okaeri.i18n.locale.StaticLocaleProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.teksusik.experiencetome.deposit.ExperienceDepositListener;
import pl.teksusik.experiencetome.i18n.BI18n;
import pl.teksusik.experiencetome.i18n.ExperienceTomeLocaleConfiguration;
import pl.teksusik.experiencetome.i18n.I18nListener;
import pl.teksusik.experiencetome.i18n.LocaleProviderType;
import pl.teksusik.experiencetome.i18n.PlayerLocaleProvider;
import pl.teksusik.experiencetome.withdraw.ExperienceWithdrawListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;

public class ExperienceTomePlugin extends JavaPlugin {
    private ExperienceTomeConfiguration configuration;
    private ExperienceTomeLocaleConfiguration localeConfiguration;

    private BukkitAudiences audiences;
    private BI18n i18n;

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

        this.localeConfiguration = LocaleConfigManager.createTemplate(ExperienceTomeLocaleConfiguration.class);

        this.audiences = BukkitAudiences.create(this);
        this.i18n = new BI18n(this.audiences);

        Locale defaultLocale = Locale.getDefault();
        this.i18n.setDefaultLocale(defaultLocale);

        if (this.configuration.getLocaleProvider() == LocaleProviderType.PLAYER) {
            this.i18n.registerLocaleProvider(new PlayerLocaleProvider());
        } else {
            this.i18n.registerLocaleProvider(new StaticLocaleProvider(defaultLocale));
        }

        File langDirectory = new File(this.getDataFolder(), "lang");
        if (!langDirectory.exists()) {
            langDirectory.mkdir();
        }

        try (Stream<Path> files = Files.walk(langDirectory.toPath())
                .filter(Files::isReadable)
                .filter(Files::isRegularFile)) {
            files.forEach(path -> {
                Locale locale = Locale.forLanguageTag(removeExtension(path.getFileName().toString().replace("_", "-")));
                ExperienceTomeLocaleConfiguration localeConfiguration = ConfigManager.create(ExperienceTomeLocaleConfiguration.class, okaeriConfig -> {
                    okaeriConfig.withConfigurer(new YamlBukkitConfigurer());
                    okaeriConfig.withBindFile(path);
                    okaeriConfig.saveDefaults();
                    okaeriConfig.load();
                });
                this.i18n.registerConfig(locale, localeConfiguration);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        if (this.i18n.getConfigs().get(defaultLocale) == null) {
            this.i18n.registerConfig(defaultLocale, ConfigManager.create(ExperienceTomeLocaleConfiguration.class, okaeriConfig -> {
                okaeriConfig.withConfigurer(new YamlBukkitConfigurer());
                okaeriConfig.withBindFile(new File(langDirectory, defaultLocale + ".yml"));
                okaeriConfig.saveDefaults();
                okaeriConfig.load();
            }));
        }

        this.key = new NamespacedKey(this, "tome");

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ExperienceDepositListener(this.configuration, this.localeConfiguration, this.i18n, this.key), this);
        pluginManager.registerEvents(new ExperienceWithdrawListener(this.configuration, this.localeConfiguration, this.i18n, this.key), this);
        pluginManager.registerEvents(new I18nListener(this.configuration, this.localeConfiguration, this.i18n, this.key), this);

        ExperienceTomeRecipe recipe = new ExperienceTomeRecipe(this.configuration, this.localeConfiguration, this.i18n, this.key);
        this.getServer().addRecipe(recipe.toShapedRecipe());

        Metrics metrics = new Metrics(this, 17509);
    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
        }
    }

    private String removeExtension(String filename) {
        return filename.replaceAll("(?<!^)[.].*", "");
    }
}

