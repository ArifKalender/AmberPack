package me.Kugelbltz.amberpack.abilities;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import me.Kugelbltz.amberpack.AmberPack;
import me.Kugelbltz.amberpack.Listeners;
import me.Kugelbltz.amberpack.Utilitiy;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlazingInferno extends CombustionAbility implements AddonAbility {


    public int duration = 0;
    Location location;
    Vector dir;
    private final double damage = AmberPack.amberpack.getConfig().getDouble("AmberPack.BlazingInferno.Damage");
    private final int radius = AmberPack.amberpack.getConfig().getInt("AmberPack.BlazingInferno.Radius");
    private final int cd = AmberPack.amberpack.getConfig().getInt("AmberPack.BlazingInferno.Cooldown");

    public BlazingInferno(Player player) {
        super(player);
        location = player.getLocation();
        dir = location.getDirection();
        if (!bPlayer.isOnCooldown(this)) {
            start();
        }
    }

    public void createInferno(Location location, Particle particle, double radius) {
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        int numParticles = 100;

        double angleIncrement = 2 * Math.PI / numParticles;

        world.spawnParticle(Particle.SOUL, location, 25, radius - 1, 2, radius - 1, 0.01);

        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (entity instanceof LivingEntity) {
                if (entity != player) {
                    DamageHandler.damageEntity(entity, player, damage, this);
                    entity.setVelocity(entity.getVelocity().multiply(0));
                    ((LivingEntity) entity).setNoDamageTicks(0);
                    entity.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation(), 1, 0, 0, 0, 0);
                }
            }
        }


        for (int i = 0; i < numParticles; i++) {
            double angle = i * angleIncrement;
            double offsetX = radius * Math.cos(angle);
            double offsetZ = radius * Math.sin(angle);

            Location particleLocation = new Location(world, x + offsetX, y, z + offsetZ);

            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0.01);
        }


        Location randomLoc = Utilitiy.getRandomLocation(location, 5);
        Location randomLocAbove = new Location(randomLoc.getWorld(), randomLoc.getX(), randomLoc.getY() + 2, randomLoc.getZ());
        Utilitiy.createBeam(randomLoc, randomLocAbove, Particle.END_ROD);

    }

    @Override
    public void progress() {
        createInferno(location, Particle.FLAME, radius);
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
        return cd;
    }

    @Override
    public String getName() {
        return "BlazingInferno";
    }

    @Override
    public String getDescription() {
        return "Hold sneak to create an inferno, damaging all nearby enemies!";
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
