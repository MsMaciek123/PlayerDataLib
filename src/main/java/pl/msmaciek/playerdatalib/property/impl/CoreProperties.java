package pl.msmaciek.playerdatalib.property.impl;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import pl.msmaciek.playerdatalib.property.ProfileProperty;
import pl.msmaciek.playerdatalib.property.PropertyRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unchecked")
public final class CoreProperties {

    private CoreProperties() {}

    // Vitals
    public static final ProfileProperty<Double> HEALTH = new SimpleProperty<>(
            "health", Double.class, Player::getHealth, Player::setHealth
    );

    public static final ProfileProperty<Integer> FOOD_LEVEL = new SimpleProperty<>(
            "food_level", Integer.class, Player::getFoodLevel, Player::setFoodLevel
    );

    public static final ProfileProperty<Float> SATURATION = new SimpleProperty<>(
            "saturation", Float.class, Player::getSaturation, Player::setSaturation
    );

    public static final ProfileProperty<Integer> REMAINING_AIR = new SimpleProperty<>(
            "remaining_air", Integer.class, Player::getRemainingAir, Player::setRemainingAir
    );

    public static final ProfileProperty<Integer> FIRE_TICKS = new SimpleProperty<>(
            "fire_ticks", Integer.class, Player::getFireTicks, Player::setFireTicks
    );

    public static final ProfileProperty<Integer> FREEZE_TICKS = new SimpleProperty<>(
            "freeze_ticks", Integer.class, Player::getFreezeTicks, Player::setFreezeTicks
    );

    public static final ProfileProperty<Float> FALL_DISTANCE = new SimpleProperty<>(
            "fall_distance", Float.class, Player::getFallDistance, Player::setFallDistance
    );

    // Experience
    public static final ProfileProperty<Float> XP = new SimpleProperty<>(
            "xp", Float.class, Player::getExp, Player::setExp
    );

    public static final ProfileProperty<Integer> LEVEL = new SimpleProperty<>(
            "level", Integer.class, Player::getLevel, Player::setLevel
    );

    // Movement
    public static final ProfileProperty<Location> LOCATION = new SimpleProperty<>(
            "location", Location.class, Player::getLocation, Player::teleport
    );

    public static final ProfileProperty<Float> WALK_SPEED = new SimpleProperty<>(
            "walk_speed", Float.class, Player::getWalkSpeed, Player::setWalkSpeed
    );

    public static final ProfileProperty<Float> FLY_SPEED = new SimpleProperty<>(
            "fly_speed", Float.class, Player::getFlySpeed, Player::setFlySpeed
    );

    public static final ProfileProperty<Boolean> ALLOW_FLIGHT = new SimpleProperty<>(
            "allow_flight", Boolean.class, Player::getAllowFlight, Player::setAllowFlight
    );

    public static final ProfileProperty<Boolean> FLYING = new SimpleProperty<>(
            "flying", Boolean.class, Player::isFlying,
            (player, flying) -> {
                // Ensure allow flight is enabled before setting flying to true
                if (flying && !player.getAllowFlight()) {
                    player.setAllowFlight(true);
                }
                player.setFlying(flying);
            }
    );

    public static final ProfileProperty<Boolean> GRAVITY = new SimpleProperty<>(
            "gravity", Boolean.class, Player::hasGravity, Player::setGravity
    );

    public static final ProfileProperty<Vector> VELOCITY = new SimpleProperty<>(
            "velocity", Vector.class, Player::getVelocity, Player::setVelocity
    );

    // State
    public static final ProfileProperty<GameMode> GAMEMODE = new SimpleProperty<>(
            "gamemode", GameMode.class, Player::getGameMode, Player::setGameMode
    );

    public static final ProfileProperty<UUID> SPECTATOR_TARGET = new SimpleProperty<>(
            "spectator_target", UUID.class,
            player -> {
                Entity target = player.getSpectatorTarget();
                return target != null ? target.getUniqueId() : null;
            },
            (player, targetUuid) -> {
                if (targetUuid == null) {
                    player.setSpectatorTarget(null);
                    return;
                }
                // Only set spectator target if player is in spectator mode
                if (player.getGameMode() != GameMode.SPECTATOR) {
                    player.setGameMode(GameMode.SPECTATOR);
                }
                Entity target = player.getServer().getEntity(targetUuid);
                // Only set if target entity exists and is alive (not dead)
                if (target != null && !target.isDead()) {
                    player.setSpectatorTarget(target);
                }
            }
    );

    public static final ProfileProperty<Boolean> GLOWING = new SimpleProperty<>(
            "glowing", Boolean.class, Player::isGlowing, Player::setGlowing
    );

    public static final ProfileProperty<Boolean> VISUAL_FIRE = new SimpleProperty<>(
            "visual_fire", Boolean.class, Player::isVisualFire, Player::setVisualFire
    );

    public static final ProfileProperty<Boolean> COLLIDABLE = new SimpleProperty<>(
            "collidable", Boolean.class, Player::isCollidable, Player::setCollidable
    );

    public static final ProfileProperty<Boolean> SILENT = new SimpleProperty<>(
            "silent", Boolean.class, Player::isSilent, Player::setSilent
    );

    public static final ProfileProperty<Boolean> INVULNERABLE = new SimpleProperty<>(
            "invulnerable", Boolean.class, Player::isInvulnerable, Player::setInvulnerable
    );

    public static final ProfileProperty<Boolean> INVISIBLE = new SimpleProperty<>(
            "invisible", Boolean.class, Player::isInvisible, Player::setInvisible
    );

    public static final ProfileProperty<Boolean> SLEEPING_IGNORED = new SimpleProperty<>(
            "sleeping_ignored", Boolean.class, Player::isSleepingIgnored, Player::setSleepingIgnored
    );

    // Inventory
    public static final ProfileProperty<Map<Integer, ItemStack>> INVENTORY_CONTENTS = new SimpleProperty<>(
            "inventory_contents",
            (Class<Map<Integer, ItemStack>>) (Class<?>) Map.class,
            player -> {
                Map<Integer, ItemStack> slots = new HashMap<>();
                for (int i = 0; i < 36; i++) {
                    ItemStack item = player.getInventory().getItem(i);
                    if (item != null) slots.put(i, item.clone());
                }
                return slots;
            },
            (player, slots) -> slots.forEach((slot, item) -> player.getInventory().setItem(slot, item))
    );

    public static final ProfileProperty<Map<Integer, ItemStack>> ARMOR_CONTENTS = new SimpleProperty<>(
            "armor_contents",
            (Class<Map<Integer, ItemStack>>) (Class<?>) Map.class,
            player -> {
                Map<Integer, ItemStack> slots = new HashMap<>();
                ItemStack[] armor = player.getInventory().getArmorContents();
                for (int i = 0; i < armor.length; i++) {
                    if (armor[i] != null) slots.put(i, armor[i].clone());
                }
                return slots;
            },
            (player, slots) -> slots.forEach((slot, item) -> {
                switch (slot) {
                    case 0 -> player.getInventory().setBoots(item);
                    case 1 -> player.getInventory().setLeggings(item);
                    case 2 -> player.getInventory().setChestplate(item);
                    case 3 -> player.getInventory().setHelmet(item);
                }
            })
    );

    public static final ProfileProperty<ItemStack> OFFHAND = new SimpleProperty<>(
            "offhand", ItemStack.class,
            player -> {
                ItemStack item = player.getInventory().getItemInOffHand();
                return item.getType().isAir() ? null : item.clone();
            },
            (player, item) -> player.getInventory().setItemInOffHand(item)
    );

    // Misc
    public static final ProfileProperty<Double> ABSORPTION_AMOUNT = new SimpleProperty<>(
            "absorption_amount", Double.class, Player::getAbsorptionAmount, Player::setAbsorptionAmount
    );

    public static final ProfileProperty<Integer> ARROW_COOLDOWN = new SimpleProperty<>(
            "arrow_cooldown", Integer.class, Player::getArrowCooldown, Player::setArrowCooldown
    );

    public static final ProfileProperty<Integer> ARROWS_IN_BODY = new SimpleProperty<>(
            "arrows_in_body", Integer.class, Player::getArrowsInBody, Player::setArrowsInBody
    );

    public static final ProfileProperty<Integer> BEE_STINGERS_IN_BODY = new SimpleProperty<>(
            "bee_stingers_in_body", Integer.class, Player::getBeeStingersInBody, Player::setBeeStingersInBody
    );

    public static final ProfileProperty<Integer> ENCHANTMENT_SEED = new SimpleProperty<>(
            "enchantment_seed", Integer.class, Player::getEnchantmentSeed, Player::setEnchantmentSeed
    );

    public static final ProfileProperty<Float> EXHAUSTION = new SimpleProperty<>(
            "exhaustion", Float.class, Player::getExhaustion, Player::setExhaustion
    );

    public static final ProfileProperty<Integer> PORTAL_COOLDOWN = new SimpleProperty<>(
            "portal_cooldown", Integer.class, Player::getPortalCooldown, Player::setPortalCooldown
    );

    public static final ProfileProperty<Integer> NO_DAMAGE_TICKS = new SimpleProperty<>(
            "no_damage_ticks", Integer.class, Player::getNoDamageTicks, Player::setNoDamageTicks
    );

    public static final ProfileProperty<Location> COMPASS_TARGET = new SimpleProperty<>(
            "compass_target", Location.class, Player::getCompassTarget, Player::setCompassTarget
    );

    public static final ProfileProperty<Location> RESPAWN_LOCATION = new SimpleProperty<>(
            "respawn_location", Location.class, Player::getRespawnLocation,
            (player, loc) -> player.setRespawnLocation(loc, true)
    );

    public static void registerAll() {
        PropertyRegistry registry = PropertyRegistry.getInstance();

        registry.register(HEALTH);
        registry.register(FOOD_LEVEL);
        registry.register(SATURATION);
        registry.register(REMAINING_AIR);
        registry.register(FIRE_TICKS);
        registry.register(FREEZE_TICKS);
        registry.register(FALL_DISTANCE);
        registry.register(XP);
        registry.register(LEVEL);
        registry.register(LOCATION);
        registry.register(WALK_SPEED);
        registry.register(FLY_SPEED);
        registry.register(ALLOW_FLIGHT);
        registry.register(FLYING);
        registry.register(GRAVITY);
        registry.register(VELOCITY);
        registry.register(GAMEMODE);
        registry.register(SPECTATOR_TARGET);
        registry.register(GLOWING);
        registry.register(VISUAL_FIRE);
        registry.register(COLLIDABLE);
        registry.register(SILENT);
        registry.register(INVULNERABLE);
        registry.register(INVISIBLE);
        registry.register(SLEEPING_IGNORED);
        registry.register(INVENTORY_CONTENTS);
        registry.register(ARMOR_CONTENTS);
        registry.register(OFFHAND);
        registry.register(ABSORPTION_AMOUNT);
        registry.register(ARROW_COOLDOWN);
        registry.register(ARROWS_IN_BODY);
        registry.register(BEE_STINGERS_IN_BODY);
        registry.register(ENCHANTMENT_SEED);
        registry.register(EXHAUSTION);
        registry.register(PORTAL_COOLDOWN);
        registry.register(NO_DAMAGE_TICKS);
        registry.register(COMPASS_TARGET);
        registry.register(RESPAWN_LOCATION);
    }
}
