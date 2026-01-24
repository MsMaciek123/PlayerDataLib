package pl.msmaciek.playerdatalib.profile;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.msmaciek.playerdatalib.property.ProfileProperty;
import pl.msmaciek.playerdatalib.property.PropertyRegistry;

import java.util.*;
import java.util.function.Predicate;

@Getter
public final class PlayerProfile {

    private final Map<String, Object> values;

    /**
     * Defines the order in which properties should be applied.
     * Properties listed first will be applied first.
     * Properties not in this list will be applied in iteration order after these.
     *
     * Important ordering constraints:
     * - gamemode must be applied before spectator_target (to enter spectator mode before setting target)
     * - allow_flight must be applied before flying
     * Note: SPECTATOR_TARGET property handles clearing even when not in spectator mode
     */
    private static final List<String> PRIORITY_ORDER = List.of(
            "gamemode",
            "allow_flight",
            "flying",
            "spectator_target"
    );

    PlayerProfile(@NotNull Map<String, Object> values) {
        this.values = Map.copyOf(values);
    }

    public Set<String> getIncludedProperties() {
        return values.keySet();
    }

    public boolean hasProperty(@NotNull String propertyId) {
        return values.containsKey(propertyId);
    }

    public boolean hasProperty(@NotNull ProfileProperty<?> property) {
        return values.containsKey(property.getId());
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getValue(@NotNull String propertyId) {
        Object value = values.get(propertyId);
        if (value == PlayerProfileBuilder.NULL_VALUE) {
            return null;
        }
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getValue(@NotNull ProfileProperty<T> property) {
        Object value = values.get(property.getId());
        if (value == PlayerProfileBuilder.NULL_VALUE) {
            return null;
        }
        return (T) value;
    }

    public void apply(@NotNull Player player) {
        PropertyRegistry registry = PropertyRegistry.getInstance();
        Set<String> applied = new HashSet<>();

        // Special case: if clearing spectator_target (setting to null) and changing gamemode,
        // we need to clear spectator target FIRST while still in spectator mode
        if (values.containsKey("spectator_target") && values.containsKey("gamemode")) {
            Object spectatorTargetValue = unwrapNullValue(values.get("spectator_target"));
            if (spectatorTargetValue == null && player.getGameMode() == GameMode.SPECTATOR) {
                ProfileProperty<?> property = registry.get("spectator_target");
                if (property != null) {
                    applyProperty(player, property, null);
                    applied.add("spectator_target");
                }
            }
        }

        // First apply properties in priority order
        for (String priorityKey : PRIORITY_ORDER) {
            if (values.containsKey(priorityKey) && !applied.contains(priorityKey)) {
                ProfileProperty<?> property = registry.get(priorityKey);
                if (property != null) {
                    Object value = values.get(priorityKey);
                    applyProperty(player, property, unwrapNullValue(value));
                    applied.add(priorityKey);
                }
            }
        }

        // Then apply remaining properties
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (applied.contains(entry.getKey())) continue;

            ProfileProperty<?> property = registry.get(entry.getKey());
            if (property == null) continue;

            applyProperty(player, property, unwrapNullValue(entry.getValue()));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void applyProperty(@NotNull Player player, @NotNull ProfileProperty<T> property, @Nullable Object value) {
        property.apply(player, (T) value);
    }

    /**
     * Converts NULL_VALUE sentinel back to actual null.
     */
    private Object unwrapNullValue(Object value) {
        return value == PlayerProfileBuilder.NULL_VALUE ? null : value;
    }

    /**
     * Applies only the properties from this profile that differ from the player's current state.
     * Useful for updating players when a global profile changes without reapplying unchanged properties.
     */
    public void applyDiff(@NotNull Player player) {
        PropertyRegistry registry = PropertyRegistry.getInstance();
        Set<String> applied = new HashSet<>();

        for (String priorityKey : PRIORITY_ORDER) {
            if (values.containsKey(priorityKey) && !applied.contains(priorityKey)) {
                ProfileProperty<?> property = registry.get(priorityKey);
                if (property != null) {
                    Object rawValue = values.get(priorityKey);
                    Object value = unwrapNullValue(rawValue);
                    if (shouldApplyProperty(player, property, value)) {
                        applyProperty(player, property, value);
                        applied.add(priorityKey);
                    }
                }
            }
        }

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (applied.contains(entry.getKey())) continue;

            ProfileProperty<?> property = registry.get(entry.getKey());
            if (property == null) continue;

            Object value = unwrapNullValue(entry.getValue());
            if (shouldApplyProperty(player, property, value)) {
                applyProperty(player, property, value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> boolean shouldApplyProperty(@NotNull Player player, @NotNull ProfileProperty<T> property, @Nullable Object newValue) {
        T currentValue = property.capture(player);
        return !Objects.equals(currentValue, newValue);
    }

    /**
     * Applies this profile to all online players matching the given predicate.
     * Only properties that differ from the player's current state are applied.
     */
    public void applyToMatching(@NotNull Predicate<Player> matcher) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (matcher.test(player)) {
                applyDiff(player);
            }
        }
    }

    /**
     * Applies this profile to all online players.
     * Only properties that differ from the player's current state are applied.
     */
    public void applyToAll() {
        applyToMatching(p -> true);
    }

    public static PlayerProfileBuilder builder() {
        return new PlayerProfileBuilder();
    }

    /**
     * Captures the current state of a player for all registered properties.
     */
    public static PlayerProfile capture(@NotNull Player player) {
        PropertyRegistry registry = PropertyRegistry.getInstance();
        PlayerProfileBuilder builder = builder();

        for (ProfileProperty<?> property : registry.getAll()) {
            Object value = property.capture(player);
            builder.set(property.getId(), value);
        }

        return builder.build();
    }

    /**
     * Captures the current state of a player for specific properties.
     */
    public static PlayerProfile capture(@NotNull Player player, @NotNull ProfileProperty<?>... properties) {
        PlayerProfileBuilder builder = builder();

        for (ProfileProperty<?> property : properties) {
            Object value = property.capture(player);
            builder.set(property.getId(), value);
        }

        return builder.build();
    }

    /**
     * Captures the current state of a player for specific property IDs.
     */
    public static PlayerProfile capture(@NotNull Player player, @NotNull String... propertyIds) {
        PropertyRegistry registry = PropertyRegistry.getInstance();
        PlayerProfileBuilder builder = builder();

        for (String propertyId : propertyIds) {
            ProfileProperty<?> property = registry.get(propertyId);
            if (property != null) {
                Object value = property.capture(player);
                builder.set(propertyId, value);
            }
        }

        return builder.build();
    }
}
