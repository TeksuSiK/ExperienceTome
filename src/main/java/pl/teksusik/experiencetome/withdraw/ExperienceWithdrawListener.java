package pl.teksusik.experiencetome.withdraw;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.teksusik.experiencetome.ExperienceTomeConfiguration;
import pl.teksusik.experiencetome.i18n.BI18n;
import pl.teksusik.experiencetome.i18n.ExperienceTomeLocaleConfiguration;

import java.util.Arrays;

public class ExperienceWithdrawListener implements Listener {
    private final ExperienceTomeConfiguration configuration;
    private final ExperienceTomeLocaleConfiguration localeConfiguration;
    private final BI18n i18n;
    private final NamespacedKey key;

    public ExperienceWithdrawListener(ExperienceTomeConfiguration configuration, ExperienceTomeLocaleConfiguration localeConfiguration, BI18n i18n, NamespacedKey key) {
        this.configuration = configuration;
        this.localeConfiguration = localeConfiguration;
        this.i18n = i18n;
        this.key = key;
    }

    @EventHandler
    public void onExperienceWithdraw(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            return;
        }

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        if (!event.hasItem()) {
            return;
        }

        if (event.getItem().getType() != this.configuration.getTomeMaterial()) {
            return;
        }

        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        if (!data.has(key, PersistentDataType.INTEGER)) {
            return;
        }

        if (item.getAmount() != 1) {
            this.i18n.get(this.localeConfiguration.getMultipleExperienceTomes())
                    .sendTo(player);
            return;
        }

        int storedExperience = data.get(key, PersistentDataType.INTEGER);
        if (storedExperience <= 0) {
            this.i18n.get(this.localeConfiguration.getExperienceTomeEmpty())
                    .sendTo(player);
            return;
        }

        ExperienceWithdrawEvent withdrawEvent = new ExperienceWithdrawEvent(player, storedExperience);
        Bukkit.getServer().getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        data.set(key, PersistentDataType.INTEGER, 0);

        String displayName = this.i18n.get(this.localeConfiguration.getDisplayName())
                .apply(player);
        if (!meta.getDisplayName().equals(displayName)) {
            meta.setDisplayName(displayName);
        }

        String lore = this.i18n.get(this.localeConfiguration.getLore())
                .with("stored_experience", 0)
                .with("maximum_experience", this.configuration.getMaximumExperience())
                .apply(player);

        meta.setLore(Arrays.stream(lore.split("\n")).toList());
        item.setItemMeta(meta);

        player.giveExp(storedExperience);
    }
}
