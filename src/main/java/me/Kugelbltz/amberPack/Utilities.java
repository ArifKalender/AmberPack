package me.Kugelbltz.amberPack;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public static List<Location> generateCircle(Location center, double radius, int points) {
        List<Location> locations = new ArrayList<>();
        double angleIncrement = 2 * Math.PI / points;

        for (int i = 0; i < points; i++) {
            double angle = i * angleIncrement;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            // Keep the same Y level as the center
            locations.add(new Location(center.getWorld(), x, center.getY(), z));
        }

        return locations;
    }

    public static double randomDouble(double origin, double limit){
        return origin + (float)(Math.random() * ((limit - origin) + 1));
    }
}
