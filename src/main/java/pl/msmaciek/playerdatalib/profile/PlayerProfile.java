package pl.msmaciek.playerdatalib.profile;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.msmaciek.playerdatalib.property.ProfileProperty;
import pl.msmaciek.playerdatalib.property.PropertyRegistry;

import java.util.Map;
import java.util.Set;

public final class PlayerProfile {

    private final Map<String, Object> values;

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

    public Map<String, Object> getValues() {
        return values;
    }

    public void apply(@NotNull Player player) {
        PropertyRegistry registry = PropertyRegistry.getInstance();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
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
