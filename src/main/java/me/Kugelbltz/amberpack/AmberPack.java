package me.Kugelbltz.amberpack;

import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AmberPack extends JavaPlugin {

    public static String version = "AmberPack-1.1";
    public static Plugin amberpack;

    @Override
    public void onEnable() {
        amberpack = this;
        this.saveDefaultConfig();
        CoreAbility.registerPluginAbilities(this, "me.Kugelbltz.amberpack.abilities");
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getServer().getPluginCommand("reloadamber").setExecutor(new ReloadAmber());
    }

    @Override
    public void onDisable() {

    }
}
