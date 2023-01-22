package pl.teksusik.experiencetome;

import org.bukkit.entity.Player;

/**
 * @see <a href="https://minecraft.fandom.com/wiki/Experience#Leveling_up">Experience - Minecraft Wiki</a>
 */
public class ExperienceHelper {
    public static int getExperience(Player player) {
        return getExperienceFromLevel(player.getLevel()) + Math.round(getExperienceToNextLevel(player.getLevel()) * player.getExp());
    }

    private static int getExperienceFromLevel(int level) {
        if (level > 30) {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
        if (level > 15) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return level * level + 6 * level;
    }

    private static int getExperienceToNextLevel(int level) {
        if (level >= 30) {
            // Simplified formula. Internal: 112 + (level - 30) * 9
            return level * 9 - 158;
        }
        if (level >= 15) {
            // Simplified formula. Internal: 37 + (level - 15) * 5
            return level * 5 - 38;
        }
        // Internal: 7 + level * 2
        return level * 2 + 7;
    }
}
