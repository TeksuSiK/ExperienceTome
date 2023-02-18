package pl.teksusik.experiencetome.i18n;

import eu.okaeri.i18n.core.minecraft.adventure.AdventureMessage;
import eu.okaeri.i18n.message.MessageDispatcher;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.Map;

public class BukkitMessageDispatcher implements MessageDispatcher<AdventureMessage> {
    private final BI18n i18n;
    private final BukkitAudiences audiences;
    private final String key;
    private final Map<String, Object> fields = new LinkedHashMap<>();

    public BukkitMessageDispatcher(BI18n i18n, BukkitAudiences audiences, String key) {
        this.i18n = i18n;
        this.audiences = audiences;
        this.key = key;
    }

    public BukkitMessageDispatcher with(String field, Object value) {
        this.fields.put(field, value);
        return this;
    }

    @Override
    public BukkitMessageDispatcher sendTo(Object entity) {
        if (entity instanceof CommandSender receiver) {
            return this.sendTo(receiver);
        }

        throw new UnsupportedOperationException("Unsupported receiver: " + entity.getClass());
    }

    public BukkitMessageDispatcher sendTo(CommandSender receiver) {
        AdventureMessage message = this.i18n.get(receiver, this.key);
        this.fields.forEach(message::with);

        if (message.raw().isEmpty()) {
            return this;
        }

        this.audiences.sender(receiver).sendMessage(message.component());
        return this;
    }

    public String apply(Object receiver) {
        AdventureMessage message = this.i18n.get(receiver, this.key);
        this.fields.forEach(message::with);

        if (message.raw().isEmpty()) {
            return "";
        }

        return message.apply();
    }
}
