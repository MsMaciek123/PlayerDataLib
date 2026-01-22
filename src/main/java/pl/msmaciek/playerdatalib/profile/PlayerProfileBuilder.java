package pl.msmaciek.playerdatalib.profile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.msmaciek.playerdatalib.property.ProfileProperty;

import java.util.HashMap;
import java.util.Map;

public final class PlayerProfileBuilder {

    private final Map<String, Object> values = new HashMap<>();

    PlayerProfileBuilder() {}

    public <T> PlayerProfileBuilder set(@NotNull ProfileProperty<T> property, @Nullable T value) {
        if (value == null) {
            values.remove(property.getId());
        } else {
            values.put(property.getId(), value);
        }
        return this;
    }

    public PlayerProfileBuilder set(@NotNull String propertyId, @Nullable Object value) {
        if (value == null) {
            values.remove(propertyId);
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
