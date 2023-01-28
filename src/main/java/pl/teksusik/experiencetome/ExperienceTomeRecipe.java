package pl.teksusik.experiencetome;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ExperienceTomeRecipe {
    private final NamespacedKey key;
    private final Material material;
    private final int customModelData;
    private final String displayName;
    private final List<String> lore;
    private final int maximumExperience;
    private final List<Material> crafting;

    public ExperienceTomeRecipe(NamespacedKey key, Material material, int customModelData, String displayName, List<String> lore, int maximumExperience, List<Material> crafting) {
        this.key = key;
        this.material = material;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.lore = new ArrayList<>(lore);
        this.maximumExperience = maximumExperience;
        this.crafting = crafting;
    }

    public ShapedRecipe toShapedRecipe() {
        ItemStack item = new ItemStack(this.material);
        ItemMeta meta = item.getItemMeta();

        if (!this.displayName.isEmpty()) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.displayName));
        }

        if (!this.lore.isEmpty()) {
            this.lore.replaceAll(line -> line
                    .replace('&', '§')
                    .replace("{STORED_EXPERIENCE}", "0")
                    .replace("{MAXIMUM_EXPERIENCE}", String.valueOf(maximumExperience)));
            meta.setLore(this.lore);
        }

        meta.setCustomModelData(this.customModelData);

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.key, PersistentDataType.INTEGER, 0);

        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(this.key, item);
        recipe.shape("ABC", "DEF", "GHI");

        char shape = 'A';
        for (Material craftingMaterial : this.crafting) {
            recipe.setIngredient(shape, craftingMaterial);
            shape += 1;
        }

        return recipe;
    }
}
