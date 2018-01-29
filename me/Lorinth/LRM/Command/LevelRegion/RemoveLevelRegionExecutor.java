package me.Lorinth.LRM.Command.LevelRegion;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.LevelRegionExecutor;
import me.Lorinth.LRM.Command.Objects.CustomCommandArgument;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Objects.OutputHandler;
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

    }

    @Override
    public void sendHelpMessage(Player player) {
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
        sendCommandArgumentDetails(player);
    }
}
