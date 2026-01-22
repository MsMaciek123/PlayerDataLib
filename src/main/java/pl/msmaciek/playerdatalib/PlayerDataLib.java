package pl.msmaciek.playerdatalib;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.msmaciek.playerdatalib.defaults.DefaultProfiles;
import pl.msmaciek.playerdatalib.profile.PlayerProfile;
import pl.msmaciek.playerdatalib.property.ProfileProperty;
import pl.msmaciek.playerdatalib.property.PropertyRegistry;
import pl.msmaciek.playerdatalib.property.impl.CoreProperties;

public final class PlayerDataLib {

    private static boolean initialized = false;

    private PlayerDataLib() {}

    public static void init() {
        if (initialized) return;
        CoreProperties.registerAll();
        initialized = true;
    }

    public static void apply(@NotNull Player player, @NotNull PlayerProfile profile) {
        init();
        profile.apply(player);
    }

    public static PlayerProfile capture(@NotNull Player player) {
        init();
        return PlayerProfile.capture(player);
    }

    public static PlayerProfile capture(@NotNull Player player, @NotNull ProfileProperty<?>... properties) {
        init();
        return PlayerProfile.capture(player, properties);
    }

    public static PlayerProfile capture(@NotNull Player player, @NotNull String... propertyIds) {
        init();
        return PlayerProfile.capture(player, propertyIds);
    }

    public static void registerProperty(@NotNull ProfileProperty<?> property) {
        init();
        PropertyRegistry.getInstance().register(property);
    }

    public static void unregisterProperty(@NotNull String propertyId) {
        PropertyRegistry.getInstance().unregister(propertyId);
    }

    public static void resetToVanilla(@NotNull Player player) {
        apply(player, DefaultProfiles.vanillaDefaults());
    }
}
