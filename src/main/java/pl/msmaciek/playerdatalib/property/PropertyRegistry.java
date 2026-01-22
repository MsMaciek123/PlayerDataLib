package pl.msmaciek.playerdatalib.property;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PropertyRegistry {

    private static final PropertyRegistry INSTANCE = new PropertyRegistry();

    private final Map<String, ProfileProperty<?>> properties = new HashMap<>();

    private PropertyRegistry() {}

    public static PropertyRegistry getInstance() {
        return INSTANCE;
    }

    public void register(@NotNull ProfileProperty<?> property) {
        properties.put(property.getId(), property);
    }

    public void unregister(@NotNull String propertyId) {
        properties.remove(propertyId);
    }

    @Nullable
    public ProfileProperty<?> get(@NotNull String propertyId) {
        return properties.get(propertyId);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> ProfileProperty<T> get(@NotNull String propertyId, @NotNull Class<T> type) {
        ProfileProperty<?> property = properties.get(propertyId);
        if (property != null && property.getType().equals(type)) {
            return (ProfileProperty<T>) property;
        }
        return null;
    }

    public boolean isRegistered(@NotNull String propertyId) {
        return properties.containsKey(propertyId);
    }

    public Collection<ProfileProperty<?>> getAll() {
        return properties.values();
    }
}
