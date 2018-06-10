package me.Lorinth.LRM.Listener;

import me.Lorinth.LRM.Command.ButcherExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;

public class CommandEventListener implements Listener {

    private ButcherExecutor executor = new ButcherExecutor();

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event){
        if(event.getPlayer().hasPermission("lrm.admin")){
            if(event.getMessage().startsWith("/butcher")){
                String[] args = event.getMessage().split(" ");
                args = Arrays.copyOfRange(args, 1, args.length);
                executor.safeExecute(event.getPlayer(), args);
                event.setCancelled(true);
            }
        }
    }
}
