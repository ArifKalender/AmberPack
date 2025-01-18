package me.Kugelbltz.amberPack.abilities;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.*;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import me.Kugelbltz.amberPack.AmberPack;
import me.Kugelbltz.amberPack.Utilities;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static me.Kugelbltz.amberPack.AmberPack.plugin;

public class BlazingInferno extends CombustionAbility implements ComboAbility, SubAbility, AddonAbility {

    boolean ignoreInvulnerabilityFrames;
    @Attribute(Attribute.DAMAGE)
    double damage;
    @Attribute(Attribute.DURATION)
    long duration;
    @Attribute(Attribute.COOLDOWN)
    long cooldown;
    @Attribute(Attribute.RADIUS)
    double radius;
    double particleSpeed;

    Location centre;

    public BlazingInferno(Player player) {
        super(player);
        if (bPlayer.canBendIgnoreBinds(this) && !CoreAbility.hasAbility(player, BlazingInferno.class)) {
            setFields();
            start();
        }
    }

    private void setFields() {
        ignoreInvulnerabilityFrames = plugin.getConfig().getBoolean("Abilities.BlazingInferno.IgnoreInvulnerabilityFrames");
        damage = plugin.getConfig().getDouble("Abilities.BlazingInferno.Damage");
        duration = plugin.getConfig().getLong("Abilities.BlazingInferno.Duration");
        cooldown = plugin.getConfig().getLong("Abilities.BlazingInferno.Cooldown");
        radius = plugin.getConfig().getDouble("Abilities.BlazingInferno.Radius");
        particleSpeed = (plugin.getConfig().getDouble("Abilities.BlazingInferno.ParticleSpeed")) / 16.667;
        centre = player.getLocation();

    }

    private void generateParticles() {

        for (Location point : Utilities.generateCircle(centre, radius, (int) (radius * 15))) {


            double vX = centre.toVector().subtract(point.toVector()).getX();
            double vY = centre.toVector().subtract(point.toVector()).getY();
            double vZ = centre.toVector().subtract(point.toVector()).getZ();


            if (Math.random() < 0.1) {

                if (bPlayer.hasSubElement(Element.BLUE_FIRE)) {
                    ParticleEffect.SOUL_FIRE_FLAME.display(point, 0, vX, vY, vZ, particleSpeed);

                } else {
                    ParticleEffect.FLAME.display(point, 0, vX, vY, vZ, particleSpeed);

                }
                ParticleEffect.SMOKE_NORMAL.display(point, 0, 0, 1, 0, 0.05);

            }
        }

    }

    /*
    *
    *
    * for (Location particle : Methods.generateCircle(location, 3)) {
                if (Math.random() <= 0.25) {
                    double vX = location.toVector().subtract(particle.toVector()).getX();
                    double vY = location.toVector().subtract(particle.toVector()).getY();
                    double vZ = location.toVector().subtract(particle.toVector()).getZ();
                    ParticleEffect.END_ROD.display(particle, 0, vX, vY, vZ, 0.085);
                }
            }
    * */
    @Override
    public void progress() {
        generateParticles();

        if (duration + this.getStartTime() < System.currentTimeMillis()) {
            remove();
            bPlayer.addCooldown(this);
            return;
        }

        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(centre, radius)) {
            if (entity instanceof LivingEntity && entity != player) {
                DamageHandler.damageEntity(entity, damage, this);
                entity.setVelocity(centre.toVector().subtract(entity.getLocation().toVector()).normalize().multiply(-1));
                if (ignoreInvulnerabilityFrames) {
                    ((LivingEntity) entity).setNoDamageTicks(0);
                }
            }
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
    public boolean isEnabled() {
        return AmberPack.blazingInferno;
    }

    @Override
    public String getInstructions() {
        return AmberPack.blazingInfernoInstructions;
    }

    @Override
    public String getDescription() {
        return AmberPack.blazingInfernoDescription;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public String getName() {
        return "BlazingInferno";
    }

    @Override
    public Location getLocation() {
        return centre;
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

    @Override
    public Object createNewComboInstance(Player player) {
        return new BlazingInferno(player);
    }

    @Override
    public ArrayList<ComboManager.AbilityInformation> getCombination() {
        ArrayList<ComboManager.AbilityInformation> combination = new ArrayList<ComboManager.AbilityInformation>();
        combination.add(new ComboManager.AbilityInformation("Combustion", ClickType.SHIFT_DOWN));
        combination.add(new ComboManager.AbilityInformation("Blaze", ClickType.SHIFT_UP));
        combination.add(new ComboManager.AbilityInformation("Blaze", ClickType.SHIFT_DOWN));
        combination.add(new ComboManager.AbilityInformation("Combustion", ClickType.SHIFT_UP));
        combination.add(new ComboManager.AbilityInformation("Combustion", ClickType.LEFT_CLICK));

        return combination;

    }
}
