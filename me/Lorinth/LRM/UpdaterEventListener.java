package me.Lorinth.LRM;

import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by lorinthio on 1/29/2018.
 */
public class UpdaterEventListener implements Listener{

    Updater updater;

    public UpdaterEventListener(Updater updater){
        this.updater = updater;
    }

    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(player.hasPermission("lrm.update") && updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE)
            OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "An Update is Available!");
    }

}
