package pl.teksusik.experiencetome;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.validator.annotation.Nullable;
import eu.okaeri.validator.annotation.Positive;
import eu.okaeri.validator.annotation.Size;
import org.bukkit.Material;

import java.util.List;

public class ExperienceTomeConfiguration extends OkaeriConfig {
    @Comment("Maximum amount of experience points that experience tome can hold. Visit https://minecraft.fandom.com/wiki/Experience#Leveling_up for reference")
    @Positive
    private int maximumExperience = 1395;
    @Comment("Message when experience tome is full")
    private String experienceTomeFull = "Experience tome is full";
    @Comment("Message when experience tome is empty")
    private String experienceTomeEmpty = "Experience tome is empty";
    @Comment("Choose available material from: https://jd.papermc.io/paper/1.14/org/bukkit/Material.html")
    private Material tomeMaterial = Material.BOOK;
    @Comment("Display name for experience tome")
    @Nullable
    private String displayName = "Experience Tome";
    @Comment("Available placeholders: {STORED_EXPERIENCE}, {MAXIMUM_EXPERIENCE}")
    @Nullable
    private List<String> lore = List.of("Use to deposit experience in tome", "Use while sneaking to withdrawn experience", "{STORED_EXPERIENCE}/{MAXIMUM_EXPERIENCE}");
    @Comment("Each line represents slot in crafting table from left to right. For empty slot use AIR. Size list must be equals to 9")
    @Size(min = 9, max = 9)
    private List<Material> crafting = List.of(Material.AIR, Material.ENDER_EYE, Material.AIR,
            Material.ENDER_PEARL, Material.BOOK, Material.ENDER_PEARL,
            Material.AIR, Material.ENDER_EYE, Material.AIR);

    public int getMaximumExperience() {
        return maximumExperience;
    }

    public String getExperienceTomeFull() {
        return experienceTomeFull;
    }

    public String getExperienceTomeEmpty() {
        return experienceTomeEmpty;
    }

    public Material getTomeMaterial() {
        return tomeMaterial;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<Material> getCrafting() {
        return crafting;
    }
}
