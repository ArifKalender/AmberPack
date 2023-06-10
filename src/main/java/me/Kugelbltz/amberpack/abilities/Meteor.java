package me.Kugelbltz.amberpack.abilities;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.waterbending.multiabilities.WaterArms;
import me.Kugelbltz.amberpack.AmberPack;
import me.Kugelbltz.amberpack.Listeners;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Meteor extends CombustionAbility implements AddonAbility {

    private Location location;
    private Vector dir;
    private boolean armorstandalive=false;
    Location armorStandLocation;
    public Meteor(Player player) {
        super(player);
        location = player.getLocation();
        dir = location.getDirection();
        if (!bPlayer.isOnCooldown(this)) {
            bPlayer.addCooldown(this);
            start();
        }
    }


    ArmorStand armorStand;

    @Override
    public void progress() {
        if(armorstandalive==false) {
            armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, false);
            armorstandalive = true;

            for (int i = 0; i <= 10; i++) {
                location.add(dir);
                if (i == 10) {
                    armorStandLocation=new Location(location.getWorld(), location.getX(), location.getY() + 10, location.getZ());
                    armorStand.teleport(armorStandLocation);
                    armorStand.getEquipment().setHelmet(new ItemStack(Material.OBSIDIAN, 1));
                    armorStand.setInvisible(true);
                    armorStand.setInvulnerable(true);
                    armorStand.setGravity(true);
                }

            }
        }
        location.getWorld().spawnParticle(Particle.FLAME,armorStand.getLocation(),5,0.2,0.2,0.2,0.05);
        if(armorStand.isOnGround()){
            armorStand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE,armorStand.getLocation(),3,0,0,0,0.04);
            for(Entity entity:armorStand.getWorld().getNearbyEntities(armorStand.getLocation(),5,5,5)){
                if(entity instanceof LivingEntity){
                    ((LivingEntity) entity).damage(5);
                }
            }
            armorStand.remove();
            remove();
            return;
        }
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
        return 30000;
    }

    @Override
    public String getName() {
        return "Meteor";
    }

    @Override
    public String getDescription() {
        return "Drop an explosive meteor in front of you!";
    }

    @Override
    public String getInstructions() {
        return "Left click";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new Listeners(), ProjectKorra.plugin);
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
