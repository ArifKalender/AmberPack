package me.Kugelbltz.amberPack.abilities;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.SubAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.Kugelbltz.amberPack.AmberPack;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

import static me.Kugelbltz.amberPack.AmberPack.plugin;

//oyuncuyu azcık geriye zıplatabilir
public class VolatileBlast extends CombustionAbility implements AddonAbility, SubAbility {

    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.SPEED)
    private int speed;
    @Attribute(Attribute.RANGE)
    private int range;
    private boolean controllable;
    private int weight;
    private int tick;

    Location location, origin;

    public VolatileBlast(Player player) {
        super(player);
        if (bPlayer.canBend(this) && !CoreAbility.hasAbility(player, VolatileBlast.class)) {
            setFields();
            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 3, 0);
            player.setVelocity(player.getEyeLocation().getDirection().multiply(-1));
            start();
        }
    }

    private void setFields() {
        damage = plugin.getConfig().getDouble("Abilities.VolatileBlast.Damage");
        speed = plugin.getConfig().getInt("Abilities.VolatileBlast.Speed");
        cooldown = plugin.getConfig().getLong("Abilities.VolatileBlast.Cooldown");
        range = 15;
        controllable = plugin.getConfig().getBoolean("Abilities.VolatileBlast.Controllable");
        weight = plugin.getConfig().getInt("Abilities.VolatileBlast.ProjectileWeight");
        location = player.getEyeLocation();
        origin = location.clone();
    }

    Vector dir;

    private void progressProjectile() {

        for (int i = 0; i <= speed; i++) {
            tick++;
            if (location.distance(origin) <= range) {
                if (controllable) {
                    dir = location.getDirection();
                    dir.setX((location.getDirection().getX() * weight) + player.getEyeLocation().getDirection().getX() / (weight + 1));
                    dir.setY((location.getDirection().getY() * weight) + player.getEyeLocation().getDirection().getY() / (weight + 1));
                    dir.setZ((location.getDirection().getZ() * weight) + player.getEyeLocation().getDirection().getZ() / (weight + 1));
                    dir.normalize();
                    location.setDirection(dir);
                    location.add(location.getDirection());
                } else {
                    location.add(location.getDirection());
                }
                playFirebendingParticles(location, 3, 0.05, 0.05, 0.05);
                ParticleEffect.SMOKE_LARGE.display(location, 1, 0, 0, 0, 0);
                ParticleEffect.END_ROD.display(location, 1, 0, 0, 0, 0.025);
                if (tick % 15 == 0) {
                    ParticleEffect.END_ROD.display(location, 80, 0, 0, 0, 0.125);
                    location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 3, 0);
                }
            } else {
                bPlayer.addCooldown(this);
                remove();
                return;
            }

            for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 1)) {
                if (entity instanceof LivingEntity && entity != player) {
                    DamageHandler.damageEntity(entity, damage, this);
                    ParticleEffect.EXPLOSION_LARGE.display(location, 3, 0, 0, 0, 0.125);
                    location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 3, 2);

                    bPlayer.addCooldown(this);
                    remove();
                    return;
                }
            }

            if (!location.getBlock().isPassable()) {
                ParticleEffect.EXPLOSION_LARGE.display(location, 3, 0, 0, 0, 0.125);
                location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 3, 2);
                for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 2)) {
                    if (entity instanceof LivingEntity) {
                        DamageHandler.damageEntity(entity, damage, this);
                    }
                }
                bPlayer.addCooldown(this);
                remove();
                return;
            }

        }
    }

    @Override
    public void progress() {
        progressProjectile();
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public String getName() {
        return "VolatileBlast";
    }

    @Override
    public boolean isEnabled() {
        return AmberPack.volatileBlast;
    }

    @Override
    public String getInstructions() {
        return AmberPack.volatileBlastInstructions;
    }

    @Override
    public String getDescription() {
        return AmberPack.volatileBlastDescription;
    }


    @Override
    public Location getLocation() {
        return null;
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
