package me.Kugelbltz.amberPack.abilities;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.CombustionAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.SubAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.region.RegionProtection;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import me.Kugelbltz.amberPack.AmberPack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.Kugelbltz.amberPack.AmberPack.*;
import static me.Kugelbltz.amberPack.Utilities.randomDouble;

public class DetonatingBreath extends CombustionAbility implements AddonAbility, SubAbility {

    @Attribute(Attribute.DURATION)
    private long duration;
    @Attribute(Attribute.DAMAGE)
    private double damage;
    @Attribute(Attribute.COOLDOWN)
    private long cooldown;
    @Attribute(Attribute.RANGE)
    private int range;
    Boolean blindnessSelf, blindnesssTarget;
    int blindnessDurationSelf, blindnessDurationTarget;
    int particleAmount;
    double particleSpeed;
    private double dissipation = 1.7;

    private long startTime;
    Location location, origin, target;

    public DetonatingBreath(Player player) {
        super(player);
        if (bPlayer.canBend(this) && !CoreAbility.hasAbility(player, DetonatingBreath.class)) {
            startTime = System.currentTimeMillis();
            setFields();
            start();
        }
    }

    private void setFields() {
        duration = plugin.getConfig().getLong("Abilities.DetonatingBreath.Duration");
        damage = plugin.getConfig().getDouble("Abilities.DetonatingBreath.Damage");
        cooldown = plugin.getConfig().getLong("Abilities.DetonatingBreath.Cooldown");
        range = plugin.getConfig().getInt("Abilities.DetonatingBreath.Range");
        particleAmount = plugin.getConfig().getInt("Abilities.DetonatingBreath.Visual.ParticleAmount");
        particleSpeed = plugin.getConfig().getDouble("Abilities.DetonatingBreath.Visual.ParticleSpeed");
        dissipation = plugin.getConfig().getDouble("Abilities.DetonatingBreath.Visual.Dissipation");
        blindnessSelf = plugin.getConfig().getBoolean("Abilities.DetonatingBreath.Blindness.Self");
        blindnesssTarget = plugin.getConfig().getBoolean("Abilities.DetonatingBreath.Blindness.Target");
        blindnessDurationSelf = plugin.getConfig().getInt("Abilities.DetonatingBreath.Blindness.SelfDuration");
        blindnessDurationTarget = plugin.getConfig().getInt("Abilities.DetonatingBreath.Blindness.TargetDuration");

    }

    private void removeAbility() {
        remove();
        bPlayer.addCooldown(this);
    }


    private void generateBreath() {
        origin = player.getEyeLocation();
        target = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(range));
        origin.setY(origin.getY() - 0.125);
        location=origin.clone();

        double vX = target.toVector().subtract(origin.toVector()).getX();
        double vY = target.toVector().subtract(origin.toVector()).getY();
        double vZ = target.toVector().subtract(origin.toVector()).getZ();
        for (int i = 0; i <= particleAmount; i++) {
            ParticleEffect.SMOKE_NORMAL.display(origin, 0, vX + randomDouble(-dissipation, dissipation), vY + randomDouble(-dissipation, dissipation), vZ + randomDouble(-dissipation, dissipation), particleSpeed);
        }
        origin.getWorld().playSound(origin, Sound.ENTITY_HORSE_BREATHE,1F,0.7F);

        while(location.distance(origin)<range){

            if((!location.getBlock().isPassable()) && !(location.getBlock().getType() == Material.TNT)){
                ParticleEffect.EXPLOSION_LARGE.display(location,1,0.3,0.1,0.3,0);
                location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE,0.4F,(float)(randomDouble(-0.125,0.125)));
                break;
            }

            if(location.getBlock().getType()==Material.TNT){
                location.getBlock().setType(Material.AIR);
                location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
            }else if(location.getBlock().isPassable()){
                if(!RegionProtection.isRegionProtected(player,new Location(location.getWorld(),location.getX(),location.getY()-1,location.getZ()))){
                    if(!new Location(location.getWorld(),location.getX(),location.getY()-1,location.getZ()).getBlock().isPassable()){
                        new TempBlock(new Location(location.getWorld(),location.getX(),location.getY()-1,location.getZ()).getBlock(), Material.SMOOTH_BASALT.createBlockData(),1000,this);
                    }
                }
            }

            location.add(location.getDirection());

            for(Entity entity : GeneralMethods.getEntitiesAroundPoint(location,0.4+location.distance(origin)*0.05)){
                if(entity != player && entity instanceof LivingEntity){
                    DamageHandler.damageEntity(entity,damage,this);
                    if(blindnesssTarget){
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,blindnessDurationTarget/50,1));
                    }
                }
            }
            if(blindnessSelf){
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,blindnessDurationSelf/50,1));
            }
        }
    }

    @Override
    public void progress() {
        if (!player.isSneaking() || startTime + duration < System.currentTimeMillis()) {
            removeAbility();
            return;
        }
        generateBreath();
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
        return cooldown;
    }

    @Override
    public boolean isEnabled() {
        return detonatingBreath;
    }

    @Override
    public String getName() {
        return "DetonatingBreath";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void load() {
        setFields();
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
    public String getInstructions() {
        return detonatingBreathInstructions;
    }

    @Override
    public String getDescription() {
        return detonatingBreathDescription;
    }
}
