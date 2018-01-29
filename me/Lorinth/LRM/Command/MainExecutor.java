package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Objects.OutputHandler;
import me.Lorinth.LRM.Updater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by bnra2 on 1/27/2018.
 */
public class MainExecutor implements CommandExecutor{

    private CustomCommandExecutor levelRegionExecutor = new LevelRegionExecutor();
    private CustomCommandExecutor spawnPointExecutor = new SpawnPointExecutor();
    private UpdateExecutor updateExecutor = new UpdateExecutor();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;
        if(args == null || args.length < 1){
            sendHelpMessage(player);
            return false;
        }

        String commandLabel = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if(commandLabel.equalsIgnoreCase(spawnPointExecutor.getCommandName())){
            spawnPointExecutor.execute(player, args);
        }
        else if(commandLabel.equalsIgnoreCase(levelRegionExecutor.getCommandName())){
            //levelRegionExecutor.execute(player, args);
        }
        else if(player.hasPermission("lrm.update") && commandLabel.equalsIgnoreCase(updateExecutor.getCommandName())){
            updateExecutor.execute(player, args);
        }
        else{
            sendHelpMessage(player);
        }

        return true;
    }

    private void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Command List");

        String commandPrefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " ";
        //OutputHandler.PrintCommandInfo(player, commandPrefix + levelRegionExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, commandPrefix + spawnPointExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, commandPrefix + updateExecutor.getUserFriendlyCommandText());
    }
}
