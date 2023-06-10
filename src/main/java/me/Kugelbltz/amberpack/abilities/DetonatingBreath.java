package me.Kugelbltz.amberpack.abilities;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.Kugelbltz.amberpack.AmberPack;
import me.Kugelbltz.amberpack.Listeners;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DetonatingBreath extends CombustionAbility implements AddonAbility {

    public int duration = 0;
    Location location;
    Vector dir;

    public DetonatingBreath(Player player) {
        super(player);
        location = player.getEyeLocation();
        dir = location.getDirection();
        if (!bPlayer.isOnCooldown(this)) {
            start();
        }
    }

    public void createBreath() {
        location=player.getEyeLocation();
        dir=location.getDirection();
        for (int i = 0; i <= 7; i++) {

            location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location,3,0.3,0.3,0.3,0.05);
            location.add(dir);

            if(location.getBlock().getType()!=Material.AIR){
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1, 1);
                location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE,location,3,0,0,0,0);
            }
            for (Entity entity : location.getWorld().getNearbyEntities(location, 0.3, 0.3, 0.3)) {
                if (entity instanceof LivingEntity) {
                    if (entity != player) {
                        location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1, 1);
                        DamageHandler.damageEntity(entity, player, 3, this);
                        ParticleEffect.EXPLOSION_LARGE.display(location, 3);
                    }
                }
            }
        }
    }

    @Override
    public void progress() {
        createBreath();
        duration++;
        if (duration >= 20 * 3) {
            duration = 0;
            bPlayer.addCooldown(this);
            remove();
            return;
        }
        if (!player.isSneaking()) {
            bPlayer.addCooldown(this);
            duration = 0;
            remove();
            return;
        }

        if (!player.isOnline()) {
            bPlayer.addCooldown(this);
            duration = 0;
            remove();
        }
    }

    @Override
    public boolean isSneakAbility() {
        return true;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return 8000;
    }

    @Override
    public String getName() {
        return "DetonatingBreath";
    }

    @Override
    public String getDescription() {
        return "Use your powerful breath to cause an explosive breath, filled with hydrogen!";
    }

    @Override
    public String getInstructions() {
        return "Hold sneak";
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
