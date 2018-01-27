package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.entity.Player;

/**
 * Created by bnra2 on 1/27/2018.
 */
public class LevelRegionExecutor extends CustomCommandExecutor{

    private final String CommandName = "region";
    private final String CommandDescription = "access to editing region data";

    @Override
    public String getCommandDescription() {
        return CommandDescription;
    }

    @Override
    public String getCommandName() {
        return CommandName;
    }

    public void execute(Player player, String[] args){
        if(args.length < 1){
            sendHelpMessage(player);
            return;
        }
    }

    private void sendHelpMessage(Player player){
        OutputHandler.PrintRawInfo(player, "/lrm " + CommandName);
    }
}
