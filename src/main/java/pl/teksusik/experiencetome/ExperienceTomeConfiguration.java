package pl.teksusik.experiencetome;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.bukkit.Material;

import java.util.List;

public class ExperienceTomeConfiguration extends OkaeriConfig {
    @Comment("Choose available material from: https://jd.papermc.io/paper/1.14/org/bukkit/Material.html")
    private Material tomeMaterial = Material.BOOK;
    @Comment("Display name for experience tome")
    private String displayName = "Experience Tome";
    @Comment("Available placeholders: -")
    private List<String> lore = List.of("Use to deposit experience in tome", "Use while sneaking to withdrawn experience");
    @Comment("Each line represents slot in crafting table from left to right. For empty slot use AIR")
    private List<Material> crafting = List.of(Material.AIR, Material.ENDER_EYE, Material.AIR,
            Material.ENDER_PEARL, Material.BOOK, Material.ENDER_PEARL,
            Material.AIR, Material.ENDER_EYE, Material.AIR);

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
