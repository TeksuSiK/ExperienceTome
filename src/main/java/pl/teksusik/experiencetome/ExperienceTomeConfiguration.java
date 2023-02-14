package pl.teksusik.experiencetome;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.validator.annotation.Nullable;
import eu.okaeri.validator.annotation.Positive;
import eu.okaeri.validator.annotation.Size;
import org.bukkit.Material;
import pl.teksusik.experiencetome.i18n.LocaleProviderType;

import java.util.List;

public class ExperienceTomeConfiguration extends OkaeriConfig {
    @Comment("Maximum amount of experience points that experience tome can hold. Visit https://minecraft.fandom.com/wiki/Experience#Leveling_up for reference")
    @Positive
    private int maximumExperience = 1395;
    @Comment("Choose available material from: https://jd.papermc.io/paper/1.14/org/bukkit/Material.html")
    private Material tomeMaterial = Material.BOOK;
    @Comment("Choose custom model data if you want custom model for tome item")
    @Nullable
    private int customModelData = 1;
    @Comment("Each line represents slot in crafting table from left to right. For empty slot use AIR. Size list must be equals to 9")
    @Size(min = 9, max = 9)
    private List<Material> crafting = List.of(Material.AIR, Material.ENDER_EYE, Material.AIR,
            Material.ENDER_PEARL, Material.BOOK, Material.ENDER_PEARL,
            Material.AIR, Material.ENDER_EYE, Material.AIR);
    @Comment("Choose locale provider for internationalization (STATIC, PLAYER)")
    private LocaleProviderType localeProvider = LocaleProviderType.PLAYER;

    public int getMaximumExperience() {
        return maximumExperience;
    }

    public Material getTomeMaterial() {
        return tomeMaterial;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public List<Material> getCrafting() {
        return crafting;
    }

    public LocaleProviderType getLocaleProvider() {
        return localeProvider;
    }
}
