package pl.teksusik.experiencetome;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ExperienceTomeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final int storedExperience;
    private boolean cancelled;

    public ExperienceTomeEvent(Player player, int storedExperience) {
        this.player = player;
        this.storedExperience = storedExperience;
        this.cancelled = false;
    }

    /**
     * @return player who tried to interact with tome
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return amount of stored experience in tome
     */
    public int getStoredExperience() {
        return storedExperience;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
