package me.Lorinth.LRM.Command.SpawnPoint;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Command.SpawnPointExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.OutputHandler;
import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class ListSpawnPointExecutor extends CustomCommandExecutor{

    private final SpawnPointExecutor parentExecutor;

    public ListSpawnPointExecutor(SpawnPointExecutor parent) {
        super("list", "lists all spawnpoints in your current world", null);
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        try{
            ArrayList<SpawnPoint> spawnPoints = LorinthsRpgMobs.GetSpawnPointManager().getAllSpawnPointsInWorld(player.getWorld());
            if(spawnPoints == null){
                OutputHandler.PrintCommandInfo(player, "No spawn points in current world");
                return;
            }
            OutputHandler.PrintCommandInfo(player, "[LorinthRpgMobs] : " + OutputHandler.HIGHLIGHT + "Spawn Points in world, '" + player.getWorld().getName() + "'");
            for(SpawnPoint spawnPoint : spawnPoints){
                OutputHandler.PrintCommandInfo(player, CommandConstants.DescriptionDelimeter + spawnPoint.getName());
            }
        }
        catch(Exception exception){
            OutputHandler.PrintRawError("Error occurred...");
            exception.printStackTrace();
            OutputHandler.PrintRawError(player, "An error occurred");
            sendHelpMessage(player);
        }
    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
    }
}
