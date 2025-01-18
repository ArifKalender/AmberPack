package me.Kugelbltz.amberPack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import me.Kugelbltz.amberPack.abilities.DetonatingBreath;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class DetonatingBreathListener implements Listener {

    @EventHandler
    private void onSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
            if (bPlayer.getBoundAbilityName().equalsIgnoreCase("DetonatingBreath")) {
                new DetonatingBreath(event.getPlayer());
            }
        }
    }

}
