package me.Kugelbltz.amberpack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadAmber implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if(sender.hasPermission("amberpack.reload")){
                AmberPack.amberpack.reloadConfig();
                sender.sendMessage("§aReloading AmberPack.");
            }else {
                sender.sendMessage("§cYou don't have the permission to execute this command.");
            }
        }else {
            AmberPack.amberpack.reloadConfig();
            sender.sendMessage("§aReloading AmberPack.");
        }

        return false;
    }
}
