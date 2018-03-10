package me.Lorinth.LRM.Listener;

import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Updater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *  Listener used for telling players about Updates
 */
public class UpdaterEventListener implements Listener{

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(LorinthsRpgMobs.updater != null){
            if((player.hasPermission("lrm.update") || player.isOp()) && LorinthsRpgMobs.updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE)
                OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "An Update is Available!");
        }

    }

}
