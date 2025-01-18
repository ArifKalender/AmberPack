package me.Kugelbltz.amberPack.listeners;

import com.projectkorra.projectkorra.BendingPlayer;
import me.Kugelbltz.amberPack.abilities.VolatileBlast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;

public class VolatileBlastListener implements Listener {

    @EventHandler
    private void onClick(PlayerAnimationEvent event) {
        if (event.getPlayer().isSneaking()) {
            BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
            if (bPlayer.getBoundAbilityName().equalsIgnoreCase("VolatileBlast")) {
                new VolatileBlast(event.getPlayer());
            }
        }
    }

}
