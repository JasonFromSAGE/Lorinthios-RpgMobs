package me.Lorinth.LRM.Command.SpawnPoint;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Command.SpawnPointExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Util.OutputHandler;
import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class SetSpawnPointExecutor extends CustomCommandExecutor{

    private final SpawnPointExecutor parentExecutor;

    public SetSpawnPointExecutor(SpawnPointExecutor parent){
        super("set", "creates/updates a spawnpoint with the given name", new ArrayList<CustomCommandArgument>(){{
            add(new CustomCommandArgument("name", "name of spawner to create/update", true));
            add(new CustomCommandArgument("distance", "distance to calculate level on", true));
            add(new CustomCommandArgument("level", "starting level for this spawn point", true));
            add(new CustomCommandArgument("maxlevel", "level cap for this spawn point", false));
            add(new CustomCommandArgument("centerBuffer", "center buffer to delay mobs from leveling until further from center", false));
        }});
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        Location loc = player.getLocation();
        String name = args[0];
        int distance = Integer.parseInt(args[1]);
        int startLevel = Integer.parseInt(args[2]);

        SpawnPoint spawnPoint = LorinthsRpgMobs.GetSpawnPointManager().getSpawnPointInWorldByName(loc.getWorld(), name);
        //Create
        if(spawnPoint == null){
            LorinthsRpgMobs.GetSpawnPointManager().addSpawnPointInWorld(player.getWorld(), new SpawnPoint(player.getLocation(), name, startLevel, distance));
            spawnPoint = LorinthsRpgMobs.GetSpawnPointManager().getSpawnPointInWorldByName(loc.getWorld(), name);
            if(args.length > 3){
                int maxLevel = Integer.parseInt(args[3]);
                spawnPoint.setMaxLevel(maxLevel);

                if(args.length > 4){
                    int centerBuffer = Integer.parseInt(args[4]);
                    spawnPoint.setCenterBuffer(centerBuffer);
                }
            }
            OutputHandler.PrintRawInfo(player, "Created spawn point with name, " + OutputHandler.HIGHLIGHT + name);
        }
        //Update
        else{
            spawnPoint.setCenter(loc);
            spawnPoint.setLevelDistance(distance);
            spawnPoint.setStartingLevel(startLevel);

            if(args.length > 3){
                int maxLevel = Integer.parseInt(args[3]);
                spawnPoint.setMaxLevel(maxLevel);

                if(args.length > 4){
                    int centerBuffer = Integer.parseInt(args[4]);
                    spawnPoint.setCenterBuffer(centerBuffer);
                }
            }
            OutputHandler.PrintRawInfo(player, "Updated spawn point with name, " + OutputHandler.HIGHLIGHT + name);
        }
    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
        sendCommandArgumentDetails(player);
    }
}
