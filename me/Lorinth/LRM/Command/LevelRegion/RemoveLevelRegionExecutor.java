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
public class RemoveLevelRegionExecutor extends CustomCommandExecutor{

    LevelRegionExecutor parentExecutor;

    public RemoveLevelRegionExecutor(LevelRegionExecutor parent) {
        super("remove", "marks a region for deletion on the next server reload/stop", new ArrayList<CustomCommandArgument>(){{
            add(new CustomCommandArgument("regionId", "the Id of the level region you want to delete", true));
        }});
        parentExecutor = parent;
    }


    @Override
    public void safeExecute(Player player, String[] args) {
        String name = args[0].toLowerCase();

        LevelRegion region = LorinthsRpgMobs.GetLevelRegionManager().getLevelRegionByName(player.getWorld(), name);
        if(region == null){
            OutputHandler.PrintError(player, "Level Region not found with the name, " + OutputHandler.HIGHLIGHT + name);
            return;
        }

        region.setDeleted();
        OutputHandler.PrintRawError(player, "Level Region, " + OutputHandler.HIGHLIGHT + region.getName() + OutputHandler.ERROR + ", has been removed");
    }

    @Override
    public void sendHelpMessage(Player player) {
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
        sendCommandArgumentDetails(player);
    }
}
