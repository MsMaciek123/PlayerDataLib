package pl.msmaciek.playerdatalib.property;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ProfileProperty<T> {

    @NotNull String getId();

    @Nullable T capture(@NotNull Player player);

    void apply(@NotNull Player player, @NotNull T value);

    @NotNull Class<T> getType();
}
