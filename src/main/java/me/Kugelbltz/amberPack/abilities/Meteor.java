package me.Kugelbltz.amberPack.abilities;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.ability.SubAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.region.RegionProtection;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import me.Kugelbltz.amberPack.AmberPack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.ArrayList;

import static me.Kugelbltz.amberPack.AmberPack.plugin;

public class Meteor extends CombustionAbility implements AddonAbility, SubAbility, ComboAbility {

    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute(Attribute.RANGE)
    private int range;
    private int weight;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.RADIUS)
    private double blockDamageRadius;
    @Attribute(Attribute.SPEED)
    private double speed;

    private boolean isControllable;
    Location location, forHalf, forQuarter, origin, forEightQuarter;
    BlockDisplay inner, middle, outer;

    //Fireball tarzı ama glass blockdisplayler ile yapilicak
    public Meteor(Player player) {
        super(player);
        if (bPlayer.canBendIgnoreBinds(this) && !hasAbility(player, Meteor.class)) {
            setFields();
            start();


        }

    }

    Transformation transformation;
    Vector3f vc;

    private void setFields() {
        cooldown = plugin.getConfig().getLong("Abilities.Meteor.Cooldown");
        range = plugin.getConfig().getInt("Abilities.Meteor.Range");
        weight = plugin.getConfig().getInt("Abilities.Meteor.ProjectileWeight");
        damage = plugin.getConfig().getDouble("Abilities.Meteor.Damage");
        blockDamageRadius = plugin.getConfig().getDouble("Abilities.Meteor.BlockDamageRadius");
        speed = plugin.getConfig().getDouble("Abilities.Meteor.Speed");
        isControllable = plugin.getConfig().getBoolean("Abilities.Meteor.Controllable");

        location = player.getEyeLocation();
        origin = location.clone();

        forEightQuarter = GeneralMethods.getRightSide(location, 0.125);
        forEightQuarter.setY(location.getY() - 0.125);
        forEightQuarter.add(location.getDirection().multiply(-0.125));
        inner = (BlockDisplay) location.getWorld().spawnEntity(forEightQuarter, EntityType.BLOCK_DISPLAY, false);
        inner.setBlock(Material.YELLOW_STAINED_GLASS.createBlockData());
        forQuarter = GeneralMethods.getRightSide(location.clone(), 0.25);
        forQuarter.setY(location.getY() - 0.25);
        forQuarter.add(location.getDirection().multiply(-0.25));
        middle = (BlockDisplay) location.getWorld().spawnEntity(forQuarter, EntityType.BLOCK_DISPLAY, false);
        middle.setBlock(Material.ORANGE_STAINED_GLASS.createBlockData());

        forHalf = GeneralMethods.getRightSide(location.clone(), 0.5);
        forHalf.setY(location.getY() - 0.5);
        forHalf.add(location.getDirection().multiply(-0.5));
        outer = (BlockDisplay) location.getWorld().spawnEntity(forHalf, EntityType.BLOCK_DISPLAY, false);
        outer.setBlock(Material.RED_STAINED_GLASS.createBlockData());

        transformation = inner.getTransformation();
        vc = transformation.getScale();
        vc.set(0.25, 0.25, 0.25);
        transformation.getScale().set(vc);
        inner.setTransformation(transformation);

        transformation = middle.getTransformation();
        vc = transformation.getScale();
        vc.set(0.5, 0.5, 0.5);
        transformation.getScale().set(vc);
        middle.setTransformation(transformation);

    }
    Vector dir;

    private void removeAbility(){

        inner.remove();
        middle.remove();
        outer.remove();
        remove();
        bPlayer.addCooldown(this);
    }
    private void moveDisplays() {

        if (location.distance(origin) < range) {
            if(isControllable){

                dir = location.getDirection();
                dir.setX((location.getDirection().getX() * weight) + player.getEyeLocation().getDirection().getX() / (weight + 1));
                dir.setY((location.getDirection().getY() * weight) + player.getEyeLocation().getDirection().getY() / (weight + 1));
                dir.setZ((location.getDirection().getZ() * weight) + player.getEyeLocation().getDirection().getZ() / (weight + 1));
                dir.normalize();
                location.setDirection(dir);
                location.add(location.getDirection().multiply(speed));


            }else{
                location.add(location.getDirection().multiply(speed));
            }
            playFirebendingParticles(location,7,0.565,0.565,0.565);
            ParticleEffect.SMOKE_NORMAL.display(location,14,0.565,0.565,0.565,0.05);
            location.getWorld().playSound(location,Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,1.1F,0F);
        } else {
            removeAbility();
            return;
        }

        if (!location.getBlock().isPassable()) {
            removeAbility();
            location.getWorld().playSound(location,Sound.ENTITY_GENERIC_EXPLODE,3,0);
            for(Block block : GeneralMethods.getBlocksAroundPoint(location,blockDamageRadius)){
                if(!RegionProtection.isRegionProtected(player,block.getLocation(),this)){
                    if(!block.isPassable() && block.getType()!=Material.BEDROCK && block.getType()!=Material.BARRIER){
                        new TempBlock(block,Material.AIR.createBlockData(),5000,this);
                        ParticleEffect.SMOKE_LARGE.display(block.getLocation(),1,0.2,0.2,0.2,0.05);
                        playFirebendingParticles(block.getLocation(),1,0.5,0.5,0.5);
                    }
                }
            }
            for(Block block : GeneralMethods.getBlocksAroundPoint(location,blockDamageRadius*1.25)){
                if(!RegionProtection.isRegionProtected(player,block.getLocation(),this)){
                    if(!block.isPassable() && block.getType()!=Material.BEDROCK && block.getType()!=Material.BARRIER){
                        new TempBlock(block,Material.SMOOTH_BASALT.createBlockData(),5000,this);
                    }
                }
            }
            for(Entity entity : GeneralMethods.getEntitiesAroundPoint(location,blockDamageRadius*0.8)) {
                if(entity instanceof LivingEntity && entity != player){
                    DamageHandler.damageEntity(entity,damage,this);
                }
            }

            return;

        }

        forEightQuarter = GeneralMethods.getRightSide(location, 0.125);
        forEightQuarter.setY(location.getY() - 0.125);
        forEightQuarter.add(location.getDirection().multiply(-0.125));
        inner.setTeleportDuration(1);
        inner.teleport(forEightQuarter);

        forQuarter = GeneralMethods.getRightSide(location.clone(), 0.25);
        forQuarter.setY(location.getY() - 0.25);
        forQuarter.add(location.getDirection().multiply(-0.25));
        middle.setTeleportDuration(1);
        middle.teleport(forQuarter);

        forHalf = GeneralMethods.getRightSide(location.clone(), 0.5);
        forHalf.setY(location.getY() - 0.5);
        forHalf.add(location.getDirection().multiply(-0.5));
        outer.setTeleportDuration(1);
        outer.teleport(forHalf);

    }

    @Override
    public void progress() {
        moveDisplays();
        for(Entity entity : GeneralMethods.getEntitiesAroundPoint(location,2)) {
            if(entity instanceof LivingEntity && entity != player){
                DamageHandler.damageEntity(entity,damage,this);
                entity.setVelocity(location.getDirection().multiply(speed*0.5));
            }
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
        return cooldown;
    }

    @Override
    public String getName() {
        return "Meteor";
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
    public String getInstructions() {
        return AmberPack.meteorInstructions;
    }

    @Override
    public String getDescription() {
        return AmberPack.meteorDescription;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public String getVersion() {
        return AmberPack.version;
    }

    @Override
    public Object createNewComboInstance(Player player) {
        return new Meteor(player);
    }

    @Override
    public ArrayList<ComboManager.AbilityInformation> getCombination() {

        ArrayList<ComboManager.AbilityInformation> combination = new ArrayList<ComboManager.AbilityInformation>();
        combination.add(new ComboManager.AbilityInformation("Combustion", ClickType.SHIFT_DOWN));
        combination.add(new ComboManager.AbilityInformation("Combustion", ClickType.SHIFT_UP));
        combination.add(new ComboManager.AbilityInformation("FireBlast", ClickType.SHIFT_DOWN));
        combination.add(new ComboManager.AbilityInformation("FireBlast", ClickType.SHIFT_UP));
        combination.add(new ComboManager.AbilityInformation("Combustion", ClickType.OFFHAND_TRIGGER));

        return combination;
    }
}
