package me.Kugelbltz.amberpack;

import com.projectkorra.projectkorra.BendingPlayer;
import me.Kugelbltz.amberpack.abilities.BlazingInferno;
import me.Kugelbltz.amberpack.abilities.DetonatingBreath;
import me.Kugelbltz.amberpack.abilities.Meteor;
import me.Kugelbltz.amberpack.abilities.VolatileBlast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);

        if (!player.isSneaking()) {
            if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("detonatingbreath")) {
                new DetonatingBreath(player);
            }if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("blazinginferno")) {
                new BlazingInferno(player);
            }
        }
    }

    @EventHandler
    public void onLeftClick(PlayerAnimationEvent event){
        Player player = event.getPlayer();
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);

            if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("meteor")) {
                new Meteor(player);
            }
        if(player.isSneaking()){
            if (bendingPlayer.getBoundAbilityName().equalsIgnoreCase("volatileblast")) {
                new VolatileBlast(player);
            }
        }
    }
}
