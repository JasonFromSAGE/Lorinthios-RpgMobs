package me.Lorinth.LRM.Command.LevelRegion;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.LevelRegionExecutor;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Data.LevelRegionManager;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.LevelRegion;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.entity.Player;

/**
 * Created by lorinthio on 1/29/2018.
 */
public class HereLevelRegionExecutor extends CustomCommandExecutor{

    LevelRegionExecutor parentExecutor;

    public HereLevelRegionExecutor(LevelRegionExecutor parent) {
        super("here", "shows the level that would be calculated at your location (and the region if applicable)", null);
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        LevelRegionManager levelRegionManager = LorinthsRpgMobs.GetLevelRegionManager();
        int level = LorinthsRpgMobs.GetLevelAtLocation(player.getLocation());
        LevelRegion levelRegion = levelRegionManager.getHighestPriorityLeveledRegionAtLocation(player.getLocation());

        OutputHandler.PrintWhiteSpace(player, 2);
        OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Level info for your location");
        OutputHandler.PrintCommandInfo(player, "Level : " + OutputHandler.HIGHLIGHT + level + (levelRegion != null ? " ( region : " + levelRegion.getName() + ")" : ""));
    }

    @Override
    public void sendHelpMessage(Player player) {
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
    }
}
