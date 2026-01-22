package pl.msmaciek.playerdatalib.defaults;

import org.bukkit.GameMode;
import pl.msmaciek.playerdatalib.profile.PlayerProfile;
import pl.msmaciek.playerdatalib.property.impl.CoreProperties;

import java.util.Collections;

public final class DefaultProfiles {

    private DefaultProfiles() {}

    public static PlayerProfile vanillaDefaults() {
        return PlayerProfile.builder()
                .set(CoreProperties.HEALTH, 20.0)
                .set(CoreProperties.FOOD_LEVEL, 20)
                .set(CoreProperties.SATURATION, 5.0f)
                .set(CoreProperties.EXHAUSTION, 0.0f)
                .set(CoreProperties.REMAINING_AIR, 300)
                .set(CoreProperties.FIRE_TICKS, -20)
                .set(CoreProperties.FREEZE_TICKS, 0)
                .set(CoreProperties.FALL_DISTANCE, 0.0f)
                .set(CoreProperties.XP, 0.0f)
                .set(CoreProperties.LEVEL, 0)
                .set(CoreProperties.WALK_SPEED, 0.2f)
                .set(CoreProperties.FLY_SPEED, 0.1f)
                .set(CoreProperties.ALLOW_FLIGHT, false)
                .set(CoreProperties.FLYING, false)
                .set(CoreProperties.GRAVITY, true)
                .set(CoreProperties.GAMEMODE, GameMode.SURVIVAL)
                .set(CoreProperties.GLOWING, false)
                .set(CoreProperties.VISUAL_FIRE, false)
                .set(CoreProperties.COLLIDABLE, true)
                .set(CoreProperties.SILENT, false)
                .set(CoreProperties.INVULNERABLE, false)
                .set(CoreProperties.INVISIBLE, false)
                .set(CoreProperties.SLEEPING_IGNORED, false)
                .set(CoreProperties.ABSORPTION_AMOUNT, 0.0)
                .set(CoreProperties.ARROWS_IN_BODY, 0)
                .set(CoreProperties.BEE_STINGERS_IN_BODY, 0)
                .set(CoreProperties.NO_DAMAGE_TICKS, 0)
                .set(CoreProperties.PORTAL_COOLDOWN, 0)
                .set(CoreProperties.INVENTORY_CONTENTS, Collections.emptyMap())
                .set(CoreProperties.ARMOR_CONTENTS, Collections.emptyMap())
                .build();
    }

    public static PlayerProfile survivalReset() {
        return PlayerProfile.builder()
                .set(CoreProperties.GAMEMODE, GameMode.SURVIVAL)
                .set(CoreProperties.HEALTH, 20.0)
                .set(CoreProperties.FOOD_LEVEL, 20)
                .set(CoreProperties.SATURATION, 5.0f)
                .set(CoreProperties.ALLOW_FLIGHT, false)
                .set(CoreProperties.FLYING, false)
                .set(CoreProperties.WALK_SPEED, 0.2f)
                .set(CoreProperties.FLY_SPEED, 0.1f)
                .set(CoreProperties.COLLIDABLE, true)
                .set(CoreProperties.INVULNERABLE, false)
                .set(CoreProperties.FIRE_TICKS, 0)
                .set(CoreProperties.FREEZE_TICKS, 0)
                .set(CoreProperties.VISUAL_FIRE, false)
                .set(CoreProperties.FALL_DISTANCE, 0.0f)
                .build();
    }

    public static PlayerProfile clearInventory() {
        return PlayerProfile.builder()
                .set(CoreProperties.INVENTORY_CONTENTS, Collections.emptyMap())
                .set(CoreProperties.ARMOR_CONTENTS, Collections.emptyMap())
                .build();
    }
}
