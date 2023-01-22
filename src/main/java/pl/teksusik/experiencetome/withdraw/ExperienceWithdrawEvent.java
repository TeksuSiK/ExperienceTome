package pl.teksusik.experiencetome.withdraw;

import org.bukkit.entity.Player;
import pl.teksusik.experiencetome.ExperienceTomeEvent;

public class ExperienceWithdrawEvent extends ExperienceTomeEvent {
    public ExperienceWithdrawEvent(Player player, int storedExperience) {
        super(player, storedExperience);
    }
}
