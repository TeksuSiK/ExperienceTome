package pl.teksusik.experiencetome.i18n;

import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.validator.annotation.Nullable;

public class ExperienceTomeLocaleConfiguration extends LocaleConfig {
    @Comment("Message when experience tome is full")
    private String experienceTomeFull = "<red>Experience tome is full";
    @Comment("Message when experience tome is empty")
    private String experienceTomeEmpty = "<red>Experience tome is empty";
    @Comment("Message when player tries to interact with more than one tome")
    private String multipleExperienceTomes = "<red>You can only use one experience tome at a time";
    @Comment("Display name for experience tome")
    @Nullable
    private String displayName = "<green>Experience Tome";
    @Comment("Available placeholders: {stored_experience}, {maximum_experience}")
    @Nullable
    private String lore = "<dark_green>Use to withdraw experience from tome<reset>" +
            "\n<dark_green>Use while sneaking to deposit experience<reset>" +
            "\n<dark_aqua>{stored_experience}/{maximum_experience}";

    public String getExperienceTomeFull() {
        return experienceTomeFull;
    }

    public String getExperienceTomeEmpty() {
        return experienceTomeEmpty;
    }

    public String getMultipleExperienceTomes() {
        return multipleExperienceTomes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLore() {
        return lore;
    }
}
