package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.entity.Player;

/**
 * Created by bnra2 on 1/27/2018.
 */
public class LevelRegionExecutor extends CustomCommandExecutor {

    public LevelRegionExecutor(){
        super("region", "access to editing region data", null);
    }

    @Override
    public void safeExecute(Player player, String[] args){

    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        OutputHandler.PrintCommandInfo(player, "/lrm " + getCommandName() + " - " + getCommandDescription());
    }
}
