package me.Lorinth.LRM.Command;


import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Util.TryParse;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButcherExecutor extends CustomCommandExecutor {

    private List<String> MonsterNames = new ArrayList<String>(){{
        add("org.bukkit.entity.phantom");
    }};

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
        boolean removeMobs = true;
        boolean removeFlying = false;
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
                else if (args[i].equalsIgnoreCase("-m"))
                    removeMobs = false;
                else if (args[i].equalsIgnoreCase("-f"))
                    removeFlying = true;
            }


        OutputHandler.PrintInfo(player, "Butchering...");
        String removeMessage = "Removing the following entities";
        if(removeMobs)
            removeMessage += ", Monsters";
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
        if(removeFlying)
            removeMessage += ", Flying";
        OutputHandler.PrintInfo(player, removeMessage);

        for(LivingEntity entity : player.getWorld().getLivingEntities()){
            if(!(entity instanceof Player)){
                if(entity instanceof  Tameable){
                    Tameable tameable = (Tameable) entity;
                    if(!tameable.isTamed())
                        removeEntity(player, radius, entity, removeAnimals, removeGolems, removeArmorStands, removeAmbient, removeVillagers, removeMobs, removeFlying);
                }
                else{
                    removeEntity(player, radius, entity, removeAnimals, removeGolems, removeArmorStands, removeAmbient, removeVillagers, removeMobs, removeFlying);
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
            boolean removeVillagers,
            boolean removeMobs,
            boolean removeFlying) {
        if(!isCloseEnough(player, entity, radius))
            return;
        if (entity instanceof Villager && removeVillagers){
            entity.remove();
            return;
        }
        if(entity instanceof ArmorStand && removeArmorStands){
            entity.remove();
            return;
        }
        if ((entity instanceof IronGolem || entity instanceof Snowman) && removeGolems){
            entity.remove();
            return;
        }
        if(entity instanceof Flying && removeFlying){
            entity.remove();
            return;
        }
        if((entity instanceof Ambient || entity instanceof Squid) && removeAmbient){
            entity.remove();
            return;
        }
        if(entity instanceof Monster && removeMobs){
            entity.remove();
            return;
        }
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
        OutputHandler.PrintCommandInfo(player, " -m" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "DONT Remove Mobs (hostile)");
        OutputHandler.PrintCommandInfo(player, " -a" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Animals");
        OutputHandler.PrintCommandInfo(player, " -g" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Iron Golems");
        OutputHandler.PrintCommandInfo(player, " -s" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Armor Stands");
        OutputHandler.PrintCommandInfo(player, " -b" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Ambient creatures");
        OutputHandler.PrintCommandInfo(player, " -v" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Villagers");
        OutputHandler.PrintCommandInfo(player, " -a" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Animals");
        OutputHandler.PrintCommandInfo(player, " -f" + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + "Remove Flying");
    }
}
