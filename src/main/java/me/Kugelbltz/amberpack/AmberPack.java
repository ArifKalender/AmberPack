package me.Kugelbltz.amberpack;

import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AmberPack extends JavaPlugin {

    @Override
    public void onEnable(){
        CoreAbility.registerPluginAbilities(this, "me.Kugelbltz.amberpack.abilities");
        getServer().getPluginManager().registerEvents(new Listeners(),this);
    }

    @Override
    public void onDisable(){

    }
}
