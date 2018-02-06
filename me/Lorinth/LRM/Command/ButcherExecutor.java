package me.Lorinth.LRM.Command;


import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class ButcherExecutor extends CustomCommandExecutor {

    public ButcherExecutor(){
        super("butcher","butchers all loaded mobs", null);
    }

    @Override
    public void safeExecute(Player player, String[] args){
        OutputHandler.PrintInfo(player, "Butchering...");
        for(Entity entity : player.getWorld().getLivingEntities()){
            if(entity instanceof Creature){
                Creature creature = (Creature) entity;
                if(creature instanceof Tameable){
                    Tameable tameable = (Tameable) creature;
                    if(!tameable.isTamed())
                        entity.remove();
                }
                else{
                    entity.remove();
                }
            }
        }
        OutputHandler.PrintInfo(player, "Done!");
    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " ";
        OutputHandler.PrintCommandInfo(player, prefix + this.getUserFriendlyCommandText());
    }
}
