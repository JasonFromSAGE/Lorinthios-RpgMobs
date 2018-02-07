package me.Lorinth.LRM.Command;


import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.ArrayList;

public class ButcherExecutor extends CustomCommandExecutor {

    public ButcherExecutor(){
        super("butcher","butchers all loaded mobs", new ArrayList<CustomCommandArgument>(){{ add(new CustomCommandArgument("radius", "the radius around you", false));}});
    }

    @Override
    public void safeExecute(Player player, String[] args){
        int radius = 0;
        if(args.length > 0 && TryParse.parseInt(args[0]))
            radius = Integer.parseInt(args[0]);

        OutputHandler.PrintInfo(player, "Butchering...");
        for(Entity entity : player.getWorld().getLivingEntities()){
            if(!(entity instanceof Player)){
                if(entity instanceof  Tameable){
                    Tameable tameable = (Tameable) entity;
                    if(!tameable.isTamed() && isCloseEnough(player, entity, radius))
                        entity.remove();
                }
                else if(isCloseEnough(player, entity, radius)){
                    entity.remove();
                }
            }
        }
        OutputHandler.PrintInfo(player, "Done!");
    }

    private boolean isCloseEnough(Player player, Entity entity, int radius){
        if(radius == 0)
            return true;

        return player.getLocation().distanceSquared(entity.getLocation()) < (radius * radius);
    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " ";
        OutputHandler.PrintCommandInfo(player, prefix + this.getUserFriendlyCommandText());
        sendCommandArgumentDetails(player);
    }
}
