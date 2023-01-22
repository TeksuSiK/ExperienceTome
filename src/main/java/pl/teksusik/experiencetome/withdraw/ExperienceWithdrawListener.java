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

public class ExperienceWithdrawListener implements Listener {
    private final ExperienceTomeConfiguration configuration;
    private final NamespacedKey key;

    public ExperienceWithdrawListener(ExperienceTomeConfiguration configuration, NamespacedKey key) {
        this.configuration = configuration;
        this.key = key;
    }

    @EventHandler
    public void onExperienceDeposit(PlayerInteractEvent event) {
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

        int storedExperience = data.get(key, PersistentDataType.INTEGER);
        if (storedExperience <= 0) {
            player.sendMessage(this.configuration.getExperienceTomeEmpty());
            return;
        }

        ExperienceWithdrawEvent withdrawEvent = new ExperienceWithdrawEvent(player, storedExperience);
        Bukkit.getServer().getPluginManager().callEvent(withdrawEvent);

        if (withdrawEvent.isCancelled()) {
            return;
        }

        data.set(key, PersistentDataType.INTEGER, 0);
        item.setItemMeta(meta);

        player.giveExp(storedExperience);
    }
}
