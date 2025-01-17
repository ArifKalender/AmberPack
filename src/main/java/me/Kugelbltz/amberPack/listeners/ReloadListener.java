package me.Kugelbltz.amberPack.listeners;

import com.projectkorra.projectkorra.event.BendingReloadEvent;
import me.Kugelbltz.amberPack.AmberPack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.Kugelbltz.amberPack.AmberPack.plugin;

public class ReloadListener implements Listener {

    @EventHandler
    public void onReload(BendingReloadEvent event){
        plugin.reloadConfig();
        AmberPack.setFields();
        event.getSender().sendMessage("§x§F§F§9§7§0§0A§x§F§F§9§0§0§0m§x§F§F§8§9§0§0b§x§F§F§8§2§0§0e§x§F§F§7§C§0§0r§x§F§F§7§5§0§0P§x§F§F§6§E§0§0a§x§F§F§6§7§0§0c§x§F§F§6§0§0§0k §x§F§F§5§2§0§0w§x§F§F§4§C§0§0a§x§F§F§4§5§0§0s §x§F§F§3§7§0§0r§x§F§F§3§0§0§0e§x§F§F§2§9§0§0l§x§F§F§2§2§0§0o§x§F§F§1§B§0§0a§x§F§F§1§5§0§0d§x§F§F§0§E§0§0e§x§F§F§0§7§0§0d§x§F§F§0§0§0§0.");
    }

}
