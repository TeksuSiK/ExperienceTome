package pl.teksusik.experiencetome.i18n;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.validator.annotation.Nullable;

public class ExperienceTomeLocaleConfiguration extends LocaleConfig {
    @Comment("Message when experience tome is full")
    private String experienceTomeFull = "<red>Experience tome is full";
    @Comment("Message when experience tome is empty")
    private String experienceTomeEmpty = "<red>Experience tome is empty";
    @Comment("Display name for experience tome")
    @Nullable
    private String displayName = "<green>Experience Tome";
    @Comment("Available placeholders: {stored_experience}, {maximum_experience}")
    @Nullable
    private String lore = "<dark_green>Use to deposit experience in tome<reset>" +
            "\n<dark_green>Use while sneaking to withdrawn experience<reset>" +
            "\n<dark_aqua>{stored_experience}/{maximum_experience}";

    public String getExperienceTomeFull() {
        return experienceTomeFull;
    }

    public String getExperienceTomeEmpty() {
        return experienceTomeEmpty;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLore() {
        return lore;
    }
}
