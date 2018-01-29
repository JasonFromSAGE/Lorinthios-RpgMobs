package me.Lorinth.LRM.Command.LevelRegion;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.LevelRegionExecutor;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.entity.Player;

/**
 * Created by lorinthio on 1/29/2018.
 */
public class HereLevelRegionExecutor extends CustomCommandExecutor{

    LevelRegionExecutor parentExecutor;

    public HereLevelRegionExecutor(LevelRegionExecutor parent) {
        super("here", "shows the level that would be calculated at your location", null);
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
    }
}
