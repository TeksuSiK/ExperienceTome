package pl.teksusik.experiencetome.i18n;

import eu.okaeri.i18n.locale.LocaleProvider;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PlayerLocaleProvider implements LocaleProvider<Player> {
    @Override
    public boolean supports(Class<?> type) {
        return Player.class.isAssignableFrom(type);
    }

    @Override
    public Locale getLocale(Player entity) {
        Locale locale = Locale.forLanguageTag(entity.getLocale().replace("_", "-"));
        if (locale == null) {
            return Locale.getDefault();
        }

        return locale;
    }
}