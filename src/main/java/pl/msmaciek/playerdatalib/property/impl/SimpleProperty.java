package pl.msmaciek.playerdatalib.property.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.msmaciek.playerdatalib.property.ProfileProperty;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SimpleProperty<T> implements ProfileProperty<T> {

    private final String id;
    private final Class<T> type;
    private final Function<Player, T> capturer;
    private final BiConsumer<Player, T> applier;

    public SimpleProperty(
            @NotNull String id,
            @NotNull Class<T> type,
            @NotNull Function<Player, T> capturer,
            @NotNull BiConsumer<Player, T> applier
    ) {
        this.id = id;
        this.type = type;
        this.capturer = capturer;
        this.applier = applier;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public T capture(@NotNull Player player) {
        return capturer.apply(player);
    }

    @Override
    public void apply(@NotNull Player player, @NotNull T value) {
        applier.accept(player, value);
    }

    @Override
    public @NotNull Class<T> getType() {
        return type;
    }
}
