package me.Lorinth.LRM.Command.LevelRegion;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Command.LevelRegionExecutor;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.LevelRegion;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by lorinthio on 1/29/2018.
 */
public class ListLevelRegionExecutor extends CustomCommandExecutor {

    LevelRegionExecutor parentExecutor;

    public ListLevelRegionExecutor(LevelRegionExecutor parent) {
        super("list", "list all leveled regions in the world you are in", null);
        parentExecutor = parent;
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        ArrayList<LevelRegion> allRegions = LorinthsRpgMobs.GetLevelRegionManager().getAllLeveledRegionsInWorld(player.getWorld());
        if(allRegions == null || allRegions.size() == 0){
            OutputHandler.PrintError(player, "No Level Regions in your current world!");
            return;
        }

        OutputHandler.PrintWhiteSpace(player, 2);
        OutputHandler.PrintCommandInfo(player, "[LorinthRpgMobs] : " + OutputHandler.HIGHLIGHT + "Level Regions in world, '" + player.getWorld().getName() + "'");
        for(LevelRegion region : allRegions){
            OutputHandler.PrintCommandInfo(player, CommandConstants.DescriptionDelimeter + region.getName());
        }
    }

    @Override
    public void sendHelpMessage(Player player) {
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
    }
}
