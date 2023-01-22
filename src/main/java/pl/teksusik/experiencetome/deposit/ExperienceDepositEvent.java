package pl.teksusik.experiencetome.deposit;

import org.bukkit.entity.Player;
import pl.teksusik.experiencetome.ExperienceTomeEvent;

public class ExperienceDepositEvent extends ExperienceTomeEvent {
    private final int playerExperience;

    public ExperienceDepositEvent(Player player, int storedExperience, int playerExperience) {
        super(player, storedExperience);
        this.playerExperience = playerExperience;
    }

    /**
     * @return experience stored by player
     */
    public int getPlayerExperience() {
        return playerExperience;
    }
}
