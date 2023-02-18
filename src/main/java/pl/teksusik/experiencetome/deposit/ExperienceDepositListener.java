package pl.teksusik.experiencetome.deposit;

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
import pl.teksusik.experiencetome.ExperienceHelper;
import pl.teksusik.experiencetome.ExperienceTomeConfiguration;
import pl.teksusik.experiencetome.i18n.BI18n;
import pl.teksusik.experiencetome.i18n.ExperienceTomeLocaleConfiguration;

import java.util.Arrays;
import java.util.Locale;

public class ExperienceDepositListener implements Listener {
    private final ExperienceTomeConfiguration configuration;
    private final ExperienceTomeLocaleConfiguration localeConfiguration;
    private final BI18n i18n;
    private final NamespacedKey key;

    public ExperienceDepositListener(ExperienceTomeConfiguration configuration, ExperienceTomeLocaleConfiguration localeConfiguration, BI18n i18n, NamespacedKey key) {
        this.configuration = configuration;
        this.localeConfiguration = localeConfiguration;
        this.i18n = i18n;
        this.key = key;
    }

    @EventHandler
    public void onExperienceDeposit(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking()) {
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

        int storedExperience = data.get(key, PersistentDataType.INTEGER);
        if (storedExperience >= this.configuration.getMaximumExperience()) {
            this.i18n.get(this.localeConfiguration.getExperienceTomeFull())
                    .sendTo(player);
            return;
        }

        int playerExperience = ExperienceHelper.getExperience(player);

        ExperienceDepositEvent depositEvent = new ExperienceDepositEvent(player, storedExperience, playerExperience);
        Bukkit.getServer().getPluginManager().callEvent(depositEvent);
        if (depositEvent.isCancelled()) {
            return;
        }

        int experienceLeftInPlayer = 0;

        int newExperience = storedExperience + playerExperience;
        if (newExperience > this.configuration.getMaximumExperience()) {
            experienceLeftInPlayer = newExperience - this.configuration.getMaximumExperience();
            newExperience = this.configuration.getMaximumExperience();
        }

        data.set(key, PersistentDataType.INTEGER, newExperience);

        String lore = this.i18n.get(this.localeConfiguration.getLore())
                .with("stored_experience", newExperience)
                .with("maximum_experience", this.configuration.getMaximumExperience())
                .apply(Locale.getDefault());

        meta.setLore(Arrays.stream(lore.split("\n")).toList());
        item.setItemMeta(meta);

        player.setLevel(0);
        player.setExp(0);
        player.giveExp(experienceLeftInPlayer);
    }
}
