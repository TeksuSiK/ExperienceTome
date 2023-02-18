package pl.teksusik.experiencetome.i18n;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pl.teksusik.experiencetome.ExperienceTomeConfiguration;

import java.util.Arrays;

public class I18nListener implements Listener {
    private final ExperienceTomeConfiguration configuration;
    private final ExperienceTomeLocaleConfiguration localeConfiguration;
    private final BI18n i18n;
    private final NamespacedKey key;

    public I18nListener(ExperienceTomeConfiguration configuration, ExperienceTomeLocaleConfiguration localeConfiguration, BI18n i18n, NamespacedKey key) {
        this.configuration = configuration;
        this.localeConfiguration = localeConfiguration;
        this.i18n = i18n;
        this.key = key;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Item item = event.getItem();
        this.updateItem(player, item.getItemStack());
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getCurrentItem() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack craftingItem = event.getCurrentItem();
        this.updateItem(player, craftingItem);
    }

    private void updateItem(Player player, ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return;
        }

        if (itemStack.getType() != this.configuration.getTomeMaterial()) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (!meta.getPersistentDataContainer().has(this.key, PersistentDataType.INTEGER)) {
            return;
        }

        if (!this.localeConfiguration.getDisplayName().isEmpty()) {
            meta.setDisplayName(this.i18n.get(this.localeConfiguration.getDisplayName())
                    .apply(player));
        }

        if (!this.localeConfiguration.getLore().isEmpty()) {
            int storedExperience = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            String lore = this.i18n.get(this.localeConfiguration.getLore())
                    .with("stored_experience", storedExperience)
                    .with("maximum_experience", this.configuration.getMaximumExperience())
                    .apply(player);
            meta.setLore(Arrays.stream(lore.split("\n")).toList());
        }

        itemStack.setItemMeta(meta);
    }
}
