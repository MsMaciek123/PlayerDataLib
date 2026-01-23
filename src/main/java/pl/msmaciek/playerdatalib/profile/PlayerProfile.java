package pl.msmaciek.playerdatalib.profile;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.msmaciek.playerdatalib.property.ProfileProperty;
import pl.msmaciek.playerdatalib.property.PropertyRegistry;

import java.util.*;

@Getter
public final class PlayerProfile {

    private final Map<String, Object> values;

    /**
     * Defines the order in which properties should be applied.
     * Properties listed first will be applied first.
     * Properties not in this list will be applied in iteration order after these.
     *
     * Important ordering constraints:
     * - gamemode must be applied before spectator_target
     * - allow_flight must be applied before flying
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
        return (T) values.get(propertyId);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getValue(@NotNull ProfileProperty<T> property) {
        return (T) values.get(property.getId());
    }

    public void apply(@NotNull Player player) {
        PropertyRegistry registry = PropertyRegistry.getInstance();
        Set<String> applied = new HashSet<>();

        // First apply properties in priority order
        for (String priorityKey : PRIORITY_ORDER) {
            if (values.containsKey(priorityKey) && !applied.contains(priorityKey)) {
                ProfileProperty<?> property = registry.get(priorityKey);
                if (property != null) {
                    applyProperty(player, property, values.get(priorityKey));
                    applied.add(priorityKey);
                }
            }
        }

        // Then apply remaining properties
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (applied.contains(entry.getKey())) continue;

            ProfileProperty<?> property = registry.get(entry.getKey());
            if (property == null) continue;

            applyProperty(player, property, entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void applyProperty(@NotNull Player player, @NotNull ProfileProperty<T> property, @NotNull Object value) {
        property.apply(player, (T) value);
    }

    public static PlayerProfileBuilder builder() {
        return new PlayerProfileBuilder();
    }

    public static PlayerProfile capture(@NotNull Player player) {
        return capture(player, PropertyRegistry.getInstance().getAll().toArray(ProfileProperty[]::new));
    }

    public static PlayerProfile capture(@NotNull Player player, @NotNull ProfileProperty<?>... properties) {
        PlayerProfileBuilder builder = new PlayerProfileBuilder();
        for (ProfileProperty<?> property : properties) {
            Object value = property.capture(player);
            if (value != null) {
                builder.set(property.getId(), value);
            }
        }
        return builder.build();
    }

    public static PlayerProfile capture(@NotNull Player player, @NotNull String... propertyIds) {
        PropertyRegistry registry = PropertyRegistry.getInstance();
        PlayerProfileBuilder builder = new PlayerProfileBuilder();

        for (String propertyId : propertyIds) {
            ProfileProperty<?> property = registry.get(propertyId);
            if (property == null) continue;

            Object value = property.capture(player);
            if (value != null) {
                builder.set(propertyId, value);
            }
        }
        return builder.build();
    }
}
