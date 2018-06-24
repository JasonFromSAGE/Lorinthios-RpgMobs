package me.Lorinth.LRM.Command;


import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ButcherExecutor extends CustomCommandExecutor {

    public ButcherExecutor(){
        super("butcher","butchers all loaded mobs",
                new ArrayList<CustomCommandArgument>(){{
                    add(new CustomCommandArgument("radius", "the radius around you to butcher within", false));
        }});
    }

    @Override
    public void safeExecute(Player player, String[] args){
        int radius = 0;
        boolean removeAnimals = false;
        boolean removeGolems = false;
        boolean removeArmorStands = false;
        boolean removeAmbient = false;
        boolean removeVillagers = false;
        if(args.length > 0 && TryParse.parseInt(args[0])) {
            radius = Integer.parseInt(args[0]);
            args = Arrays.copyOfRange(args, 1, args.length);
        }
        else{
            if(args.length > 0 && args[0].equalsIgnoreCase("help")){
                sendHelpMessage(player);
                return;
            }
        }
        if(args.length > 0)
            for(int i=0; i< args.length; i++) {
                if (args[i].equalsIgnoreCase("-a"))
                    removeAnimals = true;
                else if (args[i].equalsIgnoreCase("-g"))
                    removeGolems = true;
                else if (args[i].equalsIgnoreCase("-s"))
                    removeArmorStands = true;
                else if (args[i].equalsIgnoreCase("-b"))
                    removeAmbient = true;
                else if (args[i].equalsIgnoreCase("-v"))
                    removeVillagers = true;
            }


        OutputHandler.PrintInfo(player, "Butchering...");
        String removeMessage = "Removing the following entities, Monsters";
        if(removeAnimals)
            removeMessage += ", Aniamls";
        if(removeGolems)
            removeMessage += ", Iron Golems";
        if(removeArmorStands)
            removeMessage += ", Armor Stands";
        if(removeAmbient)
            removeMessage += ", Ambient";
        if(removeVillagers)
            removeMessage += ", Villagers";
        OutputHandler.PrintInfo(player, removeMessage);

        for(LivingEntity entity : player.getWorld().getLivingEntities()){
            if(!(entity instanceof Player)){
                if(entity instanceof  Tameable){
                    Tameable tameable = (Tameable) entity;
                    if(!tameable.isTamed())
                        removeEntity(player, radius, entity, removeAnimals, removeGolems, removeArmorStands, removeAmbient, removeVillagers);
                }
                else{
                    removeEntity(player, radius, entity, removeAnimals, removeGolems, removeArmorStands, removeAmbient, removeVillagers);
                }
            }
        }
        OutputHandler.PrintInfo(player, "Done!");
    }

    private void removeEntity(Player player, int radius, Entity entity,
            boolean removeAnimals,
            boolean removeGolems,
            boolean removeArmorStands,
            boolean removeAmbient,
            boolean removeVillagers) {
        if(!isCloseEnough(player, entity, radius))
            return;
        if (entity instanceof Villager) {
            if (removeVillagers)
                entity.remove();
            return;
        }
        else if(entity instanceof ArmorStand){
            if (removeArmorStands)
                entity.remove();
            return;
        }
        else if (entity instanceof IronGolem){
            if (removeGolems)
                entity.remove();
            return;
        }
        else if(entity instanceof Bat || entity instanceof Squid){
            if(removeAmbient)
                entity.remove();
            return;
        }
        else if(entity instanceof Monster)
            entity.remove();
        if(entity instanceof Creature && !(entity instanceof Monster) && removeAnimals){
            entity.remove();
        }
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
        OutputHandler.PrintCommandInfo(player, " -a" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Animals");
        OutputHandler.PrintCommandInfo(player, " -g" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Iron Golems");
        OutputHandler.PrintCommandInfo(player, " -s" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Armor Stands");
        OutputHandler.PrintCommandInfo(player, " -b" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Ambient creatures");
        OutputHandler.PrintCommandInfo(player, " -v" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Villagers");
    }
}
