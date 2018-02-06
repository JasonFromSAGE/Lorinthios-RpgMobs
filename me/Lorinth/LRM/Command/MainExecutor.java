package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Objects.OutputHandler;
import me.Lorinth.LRM.Updater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainExecutor implements CommandExecutor{

    private CustomCommandExecutor butcherExecutor = new ButcherExecutor();
    private CustomCommandExecutor levelRegionExecutor = new LevelRegionExecutor();
    private CustomCommandExecutor reloadExecutor = new ReloadExecutor();
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
        else if(commandLabel.equalsIgnoreCase(butcherExecutor.getCommandName())){
            butcherExecutor.execute(player, args);
        }
        else if(commandLabel.equalsIgnoreCase(levelRegionExecutor.getCommandName())){
            levelRegionExecutor.execute(player, args);
        }
        else if(commandLabel.equalsIgnoreCase(reloadExecutor.getCommandName())){
            reloadExecutor.execute(player, args);
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
        if(player.hasPermission("lrm.butcher"))
            OutputHandler.PrintCommandInfo(player, commandPrefix + butcherExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, commandPrefix + levelRegionExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, commandPrefix + reloadExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, commandPrefix + spawnPointExecutor.getUserFriendlyCommandText());
        if(player.hasPermission("lrm.update"))
            OutputHandler.PrintCommandInfo(player, commandPrefix + updateExecutor.getUserFriendlyCommandText());
    }
}
