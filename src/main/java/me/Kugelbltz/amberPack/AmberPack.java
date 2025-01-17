package me.Kugelbltz.amberPack;

import com.projectkorra.projectkorra.ability.CoreAbility;
import me.Kugelbltz.amberPack.listeners.DetonatingBreathListener;
import me.Kugelbltz.amberPack.listeners.ReloadListener;
import me.Kugelbltz.amberPack.listeners.VolatileBlastListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class AmberPack extends JavaPlugin {

    //blazingınferno kombinasyonu comb -> fshield sneak, fshield -> comb sneak, comb left click

    public static String version;
    public static Plugin plugin;
    public static boolean detonatingBreath,blazingInferno,meteor,volatileBlast;
    public static String detonatingBreathInstructions,blazingInfernoInstructions,meteorInstructions,volatileBlastInstructions;
    public static String detonatingBreathDescription, blazingInfernoDescription, meteorDescription, volatileBlastDescription;

    public static boolean serverName, serverMotd, serverIp;

    @Override
    public void onEnable() {
        plugin=this;
        /*#FF9700 -> #FF0000*/
        version="§x§F§F§9§7§0§0‧§x§F§F§8§E§0§0⁺§x§F§F§8§5§0§0˚§x§F§F§7§C§0§0༓ §x§F§F§6§B§0§0A§x§F§F§6§2§0§0m§x§F§F§5§9§0§0b§x§F§F§5§0§0§0e§x§F§F§4§7§0§0r§x§F§F§3§E§0§0P§x§F§F§3§5§0§0a§x§F§F§2§C§0§0c§x§F§F§2§4§0§0k §x§F§F§1§2§0§02§x§F§F§0§9§0§0.§x§F§F§0§0§0§00";
        setFields();
        CoreAbility.registerPluginAbilities(this,"me.Kugelbltz.amberPack.abilities");
        saveDefaultConfig();
        setListeners();
        this.getConfig().options().copyDefaults(true);
        HTTPManagement.startData();
    }

    private void setListeners(){
        plugin.getServer().getPluginManager().registerEvents(new DetonatingBreathListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new VolatileBlastListener(), this);
        plugin.getServer().getPluginManager().registerEvents(new ReloadListener(), this);
    }

    public static void setFields(){
        detonatingBreath = plugin.getConfig().getBoolean("Abilities.DetonatingBreath.Enabled");
        blazingInferno = plugin.getConfig().getBoolean("Abilities.BlazingInferno.Enabled");
        meteor = plugin.getConfig().getBoolean("Abilities.Meteor.Enabled");
        volatileBlast = plugin.getConfig().getBoolean("Abilities.VolatileBlast.Enabled");


        detonatingBreathInstructions = (String) plugin.getConfig().get("Strings.DetonatingBreath.Instructions");
        detonatingBreathDescription = (String) plugin.getConfig().get("Strings.DetonatingBreath.Description");
        blazingInfernoInstructions = (String) plugin.getConfig().get("Strings.BlazingInferno.Instructions");
        blazingInfernoDescription = (String) plugin.getConfig().get("Strings.BlazingInferno.Description");
        volatileBlastInstructions = (String) plugin.getConfig().get("Strings.VolatileBlast.Instructions");
        volatileBlastDescription = (String) plugin.getConfig().get("Strings.VolatileBlast.Description");
        meteorInstructions = (String) plugin.getConfig().get("Strings.Meteor.Instructions");
        meteorDescription = (String) plugin.getConfig().get("Strings.Meteor.Description");

        serverIp=plugin.getConfig().getBoolean("DataCollection.ServerIp");
        serverName=plugin.getConfig().getBoolean("DataCollection.ServerName");
        serverMotd=plugin.getConfig().getBoolean("DataCollection.MOTD");
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
