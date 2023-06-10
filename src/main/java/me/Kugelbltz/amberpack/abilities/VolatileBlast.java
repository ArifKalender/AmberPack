package me.Kugelbltz.amberpack.abilities;

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import me.Kugelbltz.amberpack.AmberPack;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

public class VolatileBlast extends CombustionAbility implements AddonAbility {

    ArmorStand armorStand;
    private final double damage = AmberPack.amberpack.getConfig().getDouble("AmberPack.VolatileBlast.Damage");
    private final int power = AmberPack.amberpack.getConfig().getInt("AmberPack.VolatileBlast.Power");
    private final int cd = AmberPack.amberpack.getConfig().getInt("AmberPack.VolatileBlast.Cooldown");
    private int i = 0;
    private boolean armorStandAlive = false;
    private final Location location;
    private final Location origin;
    private final Vector direction;

    public VolatileBlast(Player player) {
        super(player);
        location = player.getEyeLocation();
        origin = location.clone();
        direction = location.getDirection();
        if (!bPlayer.isOnCooldown(this)) {
            start();
        }
    }

    @Override
    public void progress() {

        bPlayer.addCooldown(this);
        i++;
        if (!armorStandAlive) {
            armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, false);
            armorStandAlive = true;
            armorStand.setInvisible(true);
            armorStand.setVelocity(direction.multiply(power));
            armorStand.getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 3, 0);
        }
        location.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, armorStand.getLocation(), 10, 0.3, 0.3, 0.3, 0.05);

        if (armorStand.isOnGround()) {
            location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, armorStand.getLocation(), 5, 0.3, 0.3, 0.3, 1);
            remove();
            armorStand.getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3, 1);
            armorStand.remove();
            for (Entity entity : armorStand.getLocation().getWorld().getNearbyEntities(armorStand.getLocation(), 3, 3, 3)) {
                if (entity instanceof LivingEntity) {
                    DamageHandler.damageEntity(entity, player, damage, this);
                }
            }
            return;
        }

        if (i >= 50) {
            remove();
            armorStand.remove();
            for (Entity entity : armorStand.getLocation().getWorld().getNearbyEntities(armorStand.getLocation(), 3, 3, 3)) {
                if (entity instanceof LivingEntity) {
                    DamageHandler.damageEntity(entity, player, damage, this);
                }
            }
        }
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Throw a powerful blast to the direction you're looking at! This blast can be manipulated by other players and forces.";
    }

    @Override
    public String getInstructions() {
        return "Sneak + left click";
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return cd;
    }

    @Override
    public String getName() {
        return "VolatileBlast";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void load() {

    }

    @Override
    public void stop() {

    }

    @Override
    public String getAuthor() {
        return "Kugelbltz";
    }

    @Override
    public String getVersion() {
        return AmberPack.version;
    }
}
