package me.Kugelbltz.amberpack;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;

import java.util.Random;

public class Utilitiy {


    public static void createBeam(Location location1, Location location2, Particle particle) {
        double distance = location1.distance(location2);
        int particles = (int) Math.ceil(distance / 0.25);

        double dx = (location2.getX() - location1.getX()) / particles;
        double dy = (location2.getY() - location1.getY()) / particles;
        double dz = (location2.getZ() - location1.getZ()) / particles;

        for (int i = 0; i < particles; i++) {
            double x = location1.getX() + dx * i;
            double y = location1.getY() + dy * i;
            double z = location1.getZ() + dz * i;
            Location particleLocation = new Location(location1.getWorld(), x, y, z);
            if (particle != null) {
                location1.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0.05);
            }
        }
    }



    public static Location getRandomLocation(Location location, double radius) {
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        double x = location.getX() + radius * Math.cos(angle);
        double z = location.getZ() + radius * Math.sin(angle);
        double y = location.getY();

        for (int i = 0; i < 10; i++) {
            y--;
            Location floorLocation = new Location(location.getWorld(), x, y, z);
            Block floorBlock = floorLocation.getBlock();

            if (floorBlock.getType().isSolid()) {
                y++;
                Location randomLocation = new Location(location.getWorld(), x, y, z);
                return randomLocation;
            }
        }
        return location;
    }

}
