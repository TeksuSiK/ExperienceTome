package pl.teksusik.experiencetome.i18n;

import eu.okaeri.i18n.configs.extended.CustomMEOCI18n;
import eu.okaeri.i18n.core.minecraft.adventure.AdventureMessage;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class BI18n extends CustomMEOCI18n<AdventureMessage> {
    private final BukkitAudiences audiences;

    public BI18n(BukkitAudiences audiences) {
        this.audiences = audiences;
    }

    @Override
    public BukkitMessageDispatcher get(String key) {
        return new BukkitMessageDispatcher(this, this.audiences, key);
    }

    @Override
    public AdventureMessage assembleMessage(Placeholders placeholders, CompiledMessage compiled) {
        return AdventureMessage.of(placeholders, compiled);
    }
}
