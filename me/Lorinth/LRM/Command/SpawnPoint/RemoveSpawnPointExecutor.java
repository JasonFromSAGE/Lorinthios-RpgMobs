package me.Lorinth.LRM.Command.SpawnPoint;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Command.SpawnPointExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.OutputHandler;
import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class RemoveSpawnPointExecutor extends CustomCommandExecutor {

    private final SpawnPointExecutor parentExecutor;

    public RemoveSpawnPointExecutor(SpawnPointExecutor parent){
        super("remove", "deletes a spawnpoint with a given name", new ArrayList<CustomCommandArgument>(){{
            add(new CustomCommandArgument("name", "name of the spawnpoint you want to delete", true));
        }});
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        try {
            Location loc = player.getLocation();
            String name = args[0];

            SpawnPoint spawnPoint = LorinthsRpgMobs.GetSpawnPointManager().getSpawnPointInWorldByName(loc.getWorld(), name);
            OutputHandler.PrintRawError(player, "Spawn point, " + OutputHandler.HIGHLIGHT + spawnPoint.getName() + OutputHandler.ERROR + ", has been removed");
            spawnPoint.setDeleted();
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