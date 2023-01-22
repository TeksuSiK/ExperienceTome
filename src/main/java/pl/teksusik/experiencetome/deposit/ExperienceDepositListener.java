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

public class ExperienceDepositListener implements Listener {
    private final ExperienceTomeConfiguration configuration;
    private final NamespacedKey key;

    public ExperienceDepositListener(ExperienceTomeConfiguration configuration, NamespacedKey key) {
        this.configuration = configuration;
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
            player.sendMessage(this.configuration.getExperienceTomeFull());
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
        item.setItemMeta(meta);

        player.setLevel(0);
        player.setExp(0);
        player.giveExp(experienceLeftInPlayer);
    }
}
