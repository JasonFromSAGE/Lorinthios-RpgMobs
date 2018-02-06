package me.Lorinth.LRM.Command.LevelRegion;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.LevelRegionExecutor;
import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.LevelRegion;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by lorinthio on 1/29/2018.
 */
public class InfoLevelRegionExecutor extends CustomCommandExecutor{

    LevelRegionExecutor parentExecutor;

    public InfoLevelRegionExecutor(LevelRegionExecutor parent) {
        super("info", "shows level information for a world guard region", new ArrayList<CustomCommandArgument>(){{
            add(new CustomCommandArgument("regionId", "the Id of the world guard region you want to view info about", true));
        }});
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        String name = args[0].toLowerCase();
        LevelRegion levelRegion = LorinthsRpgMobs.GetLevelRegionManager().getLevelRegionByName(player.getWorld(), name);

        if(levelRegion == null){
            OutputHandler.PrintError(player, "Level Region not found with the name, " + OutputHandler.HIGHLIGHT + name);
            return;
        }

        OutputHandler.PrintWhiteSpace(player, 2);
        OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Level Region Info");
        OutputHandler.PrintCommandInfo(player, "Name : " + OutputHandler.HIGHLIGHT + levelRegion.getName());
        OutputHandler.PrintCommandInfo(player, "Level : " + OutputHandler.HIGHLIGHT + levelRegion.getLevel());
    }

    @Override
    public void sendHelpMessage(Player player) {
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
        sendCommandArgumentDetails(player);
    }
}
