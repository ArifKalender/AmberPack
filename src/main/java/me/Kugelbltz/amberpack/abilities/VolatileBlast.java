package me.Kugelbltz.amberpack.abilities;

//constant aoe for 3 secs

import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.Kugelbltz.amberpack.AmberPack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VolatileBlast extends CombustionAbility implements AddonAbility {

    private int i=0;
    private boolean armorStandAlive=false;
    private Location location;
    private Location origin;
    private Vector direction;
    public VolatileBlast(Player player) {
        super(player);
        location=player.getEyeLocation();
        origin=location.clone();
        direction=location.getDirection();
        if(!bPlayer.isOnCooldown(this)){
            start();
        }
    }
    ArmorStand armorStand;
    @Override
    public void progress() {

        i++;
        if(armorStandAlive==false) {
            armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, false);
            armorStandAlive = true;
            armorStand.setInvisible(true);
            armorStand.setVelocity(direction.multiply(4));
            armorStand.getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,3,0);
        }

        location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,armorStand.getLocation(),1,0.3,0.3,0.3,1);
        if(armorStand.isOnGround()){
            remove();
            armorStand.getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,3,1);
            armorStand.remove();
            for(Entity entity:armorStand.getLocation().getWorld().getNearbyEntities(armorStand.getLocation(),3,3,3)){
                if(entity instanceof LivingEntity){
                    DamageHandler.damageEntity(entity,player,4,this);
                }
            }
            return;
        }

        if(i>=50){
            remove();
            armorStand.remove();
            for(Entity entity:armorStand.getLocation().getWorld().getNearbyEntities(armorStand.getLocation(),3,3,3)){
                if(entity instanceof LivingEntity){
                    DamageHandler.damageEntity(entity,player,4,this);
                }
            }
            return;
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
        return 0;
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
