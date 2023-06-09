package me.Kugelbltz.amberpack;

import com.projectkorra.projectkorra.BendingPlayer;
import me.Kugelbltz.amberpack.abilities.DetonatingBreath;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        Player player=event.getPlayer();
        BendingPlayer bendingPlayer=BendingPlayer.getBendingPlayer(player);

        if(!player.isSneaking()) {
            if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("detonatingbreath")) {
                new DetonatingBreath(player);
            }
        }
    }

}
