package pl.teksusik.experiencetome;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.teksusik.experiencetome.i18n.BI18n;
import pl.teksusik.experiencetome.i18n.ExperienceTomeLocaleConfiguration;

import java.util.Arrays;
import java.util.Locale;

public class ExperienceTomeRecipe {
    private final ExperienceTomeConfiguration configuration;
    private final ExperienceTomeLocaleConfiguration localeConfiguration;
    private final BI18n i18n;
    private final NamespacedKey key;

    public ExperienceTomeRecipe(ExperienceTomeConfiguration configuration, ExperienceTomeLocaleConfiguration localeConfiguration, BI18n i18n, NamespacedKey key) {
        this.configuration = configuration;
        this.localeConfiguration = localeConfiguration;
        this.i18n = i18n;
        this.key = key;
    }

    public ShapedRecipe toShapedRecipe() {
        ItemStack item = new ItemStack(this.configuration.getTomeMaterial());
        ItemMeta meta = item.getItemMeta();

        if (!this.localeConfiguration.getDisplayName().isEmpty()) {
            meta.setDisplayName(this.i18n.get(this.localeConfiguration.getDisplayName())
                    .apply(Locale.getDefault()));
        }

        if (!this.localeConfiguration.getLore().isEmpty()) {
            String lore = this.i18n.get(this.localeConfiguration.getLore())
                    .with("stored_experience", 0)
                    .with("maximum_experience", this.configuration.getMaximumExperience())
                    .apply(Locale.getDefault());
            meta.setLore(Arrays.stream(lore.split("\n")).toList());
        }

        meta.setCustomModelData(this.configuration.getCustomModelData());

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.key, PersistentDataType.INTEGER, 0);

        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(this.key, item);
        recipe.shape("ABC", "DEF", "GHI");

        char shape = 'A';
        for (Material craftingMaterial : this.configuration.getCrafting()) {
            recipe.setIngredient(shape, craftingMaterial);
            shape += 1;
        }

        return recipe;
    }
}
