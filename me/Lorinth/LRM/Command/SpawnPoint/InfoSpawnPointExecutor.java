package me.Lorinth.LRM.Command.SpawnPoint;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Command.SpawnPointExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.OutputHandler;
import me.Lorinth.LRM.Objects.SpawnPoint;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by lorinthio on 1/27/2018.
 */
public class InfoSpawnPointExecutor extends CustomCommandExecutor{
    private final SpawnPointExecutor parentExecutor;

    public InfoSpawnPointExecutor(SpawnPointExecutor parent) {
        super("info", "shows detailed info about a spawnpoint", new ArrayList<CustomCommandArgument>(){{
            add(new CustomCommandArgument("name", "name of the spawnpoint you want info about", true));
        }});
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        try {
            Location loc = player.getLocation();
            String name = args[0];

            SpawnPoint spawnPoint = LorinthsRpgMobs.GetSpawnPointManager().getSpawnPointInWorldByName(loc.getWorld(), name);
            ChatColor disabledColor = (spawnPoint.isDisabled() ? ChatColor.RED : ChatColor.GREEN);
            OutputHandler.PrintWhiteSpace(player, 2);
            OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Spawn Point Info");
            OutputHandler.PrintCommandInfo(player, "Name : " + OutputHandler.HIGHLIGHT + spawnPoint.getName());
            OutputHandler.PrintCommandInfo(player, "Disabled : " + disabledColor + spawnPoint.isDisabled());
            OutputHandler.PrintCommandInfo(player, "Location : " + OutputHandler.HIGHLIGHT + spawnPoint.getCenter().getWorld().getName() + " (" + spawnPoint.getCenter().getBlockX() + "," + spawnPoint.getCenter().getBlockZ() +")");
            OutputHandler.PrintCommandInfo(player, "Start Level : " + OutputHandler.HIGHLIGHT + spawnPoint.getStartingLevel());
            OutputHandler.PrintCommandInfo(player, "Max Level : " + OutputHandler.HIGHLIGHT + (spawnPoint.getMaxLevel() == Integer.MAX_VALUE ? "N/A" : spawnPoint.getMaxLevel()));
            OutputHandler.PrintCommandInfo(player, "Distance : " + OutputHandler.HIGHLIGHT + spawnPoint.getLevelDistance());
            OutputHandler.PrintCommandInfo(player, "Center Buffer : " + OutputHandler.HIGHLIGHT + spawnPoint.getCenterBuffer());
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
