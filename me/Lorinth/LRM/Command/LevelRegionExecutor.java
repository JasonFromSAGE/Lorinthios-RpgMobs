package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Command.LevelRegion.*;
import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class LevelRegionExecutor extends CustomCommandExecutor {

    HereLevelRegionExecutor hereLevelRegionExecutor = new HereLevelRegionExecutor(this);
    InfoLevelRegionExecutor infoLevelRegionExecutor = new InfoLevelRegionExecutor(this);
    ListLevelRegionExecutor listLevelRegionExecutor = new ListLevelRegionExecutor(this);
    RemoveLevelRegionExecutor removeLevelRegionExecutor = new RemoveLevelRegionExecutor(this);
    SetLevelRegionExecutor setLevelRegionExecutor = new SetLevelRegionExecutor(this);

    public LevelRegionExecutor(){
        super("region", "access to editing region data", null);
        RequiredArguments = 1;
    }

    @Override
    public void safeExecute(Player player, String[] args){
        String commandLabel = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if(commandLabel.equalsIgnoreCase(hereLevelRegionExecutor.getCommandName()))
            hereLevelRegionExecutor.execute(player, args);
        else if(commandLabel.equalsIgnoreCase(infoLevelRegionExecutor.getCommandName()))
            infoLevelRegionExecutor.execute(player, args);
        else if(commandLabel.equalsIgnoreCase(listLevelRegionExecutor.getCommandName()))
            listLevelRegionExecutor.execute(player, args);
        else if(commandLabel.equalsIgnoreCase(removeLevelRegionExecutor.getCommandName()))
            removeLevelRegionExecutor.execute(player, args);
        else if(commandLabel.equalsIgnoreCase(setLevelRegionExecutor.getCommandName()))
            setLevelRegionExecutor.execute(player, args);
        else
            sendHelpMessage(player);
    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " ";
        OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Level Region Command List");
        OutputHandler.PrintCommandInfo(player, prefix + this.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + hereLevelRegionExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + infoLevelRegionExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + listLevelRegionExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + removeLevelRegionExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + setLevelRegionExecutor.getUserFriendlyCommandText());
    }
}
