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
public class SetLevelRegionExecutor extends CustomCommandExecutor {

    LevelRegionExecutor parentExecutor;

    public SetLevelRegionExecutor(LevelRegionExecutor parent) {
        super("set", "Creates/Updates a level region with the given level", new ArrayList<CustomCommandArgument>(){{
            add(new CustomCommandArgument("regionId", "the Id of the level region you want to create/update", true));
            add(new CustomCommandArgument("level", "the level of the region", true));
        }});
        parentExecutor = parent;
    }


    @Override
    public void safeExecute(Player player, String[] args) {
        String name = args[0].toLowerCase();
        int level = Integer.parseInt(args[1]);

        LevelRegion region = LorinthsRpgMobs.GetLevelRegionManager().getLevelRegionByName(player.getWorld(), name);
        if(region == null){
            OutputHandler.PrintRawInfo(player, "Added level region with name, " + OutputHandler.HIGHLIGHT + name);
            LorinthsRpgMobs.GetLevelRegionManager().addLevelRegionToWorld(player.getWorld(), new LevelRegion(name, level));
        }
        else{
            region.setLevel(level);
            OutputHandler.PrintRawInfo(player, "Updated level region with name, " + OutputHandler.HIGHLIGHT + name);
        }
    }

    @Override
    public void sendHelpMessage(Player player) {
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " " + parentExecutor.getCommandName();
        OutputHandler.PrintCommandInfo(player, prefix + " " + this.getUserFriendlyCommandText());
        sendCommandArgumentDetails(player);
    }
}
