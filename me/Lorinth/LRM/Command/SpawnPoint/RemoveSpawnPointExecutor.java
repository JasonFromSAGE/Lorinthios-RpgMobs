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
public class RemoveSpawnPointExecutor extends CustomCommandExecutor {

    private final SpawnPointExecutor parentExecutor;

    public RemoveSpawnPointExecutor(SpawnPointExecutor parent){
        super("remove", "marks a spawn point ", new ArrayList<CustomCommandArgument>(){{
            add(new CustomCommandArgument("name", "name of the spawnpoint you want to delete", true));
        }});
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        Location loc = player.getLocation();
        String name = args[0];

        SpawnPoint spawnPoint = LorinthsRpgMobs.GetSpawnPointManager().getSpawnPointInWorldByName(loc.getWorld(), name);
        if(spawnPoint == null) {
            OutputHandler.PrintError(player, "Spawn point not found with the name, " + OutputHandler.HIGHLIGHT + name);
            return;
        }

        spawnPoint.setDeleted();
        OutputHandler.PrintRawError(player, "Spawn point, " + OutputHandler.HIGHLIGHT + spawnPoint.getName() + OutputHandler.ERROR + ", has been removed");
    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
        sendCommandArgumentDetails(player);
    }
}
