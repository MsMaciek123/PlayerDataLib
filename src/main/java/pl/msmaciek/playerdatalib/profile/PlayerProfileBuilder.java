package pl.msmaciek.playerdatalib.profile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.msmaciek.playerdatalib.property.ProfileProperty;

import java.util.HashMap;
import java.util.Map;

public final class PlayerProfileBuilder {

    private final Map<String, Object> values = new HashMap<>();

    /**
     * Sentinel value to represent explicit null in the profile.
     * This allows distinguishing between "not set" and "set to null".
     */
    public static final Object NULL_VALUE = new Object() {
        @Override
        public String toString() {
            return "NULL_VALUE";
        }
    };

    PlayerProfileBuilder() {}

    public <T> PlayerProfileBuilder set(@NotNull ProfileProperty<T> property, @Nullable T value) {
        if (value == null) {
            values.put(property.getId(), NULL_VALUE);
        } else {
            values.put(property.getId(), value);
        }
        return this;
    }

    public PlayerProfileBuilder set(@NotNull String propertyId, @Nullable Object value) {
        if (value == null) {
            values.put(propertyId, NULL_VALUE);
        } else {
            values.put(propertyId, value);
        }
        return this;
    }

    public PlayerProfileBuilder remove(@NotNull ProfileProperty<?> property) {
        values.remove(property.getId());
        return this;
    }

    public PlayerProfileBuilder remove(@NotNull String propertyId) {
        values.remove(propertyId);
        return this;
    }

    public PlayerProfileBuilder merge(@NotNull PlayerProfile profile) {
        values.putAll(profile.getValues());
        return this;
    }

    public PlayerProfile build() {
        return new PlayerProfile(values);
    }
}
