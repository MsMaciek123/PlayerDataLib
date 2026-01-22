package pl.msmaciek.playerdatalib.types;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public record AttributeData(
        @NotNull Attribute attribute,
        double baseValue,
        @NotNull List<AttributeModifier> modifiers
) {
    public AttributeData(@NotNull Attribute attribute, double baseValue, @NotNull List<AttributeModifier> modifiers) {
        this.attribute = attribute;
        this.baseValue = baseValue;
        this.modifiers = List.copyOf(modifiers);
    }

    public AttributeData(@NotNull Attribute attribute, double baseValue) {
        this(attribute, baseValue, Collections.emptyList());
    }
}
