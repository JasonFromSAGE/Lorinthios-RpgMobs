package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by bnra2 on 1/27/2018.
 */
public class MainExecutor implements CommandExecutor{

    private CustomCommandExecutor spawnPointExecutor = new SpawnPointExecutor();
    private CustomCommandExecutor levelRegionExecutor = new LevelRegionExecutor();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;
        if(args.length < 1){
            sendHelpMessage(player);
            return false;
        }

        String commandLabel = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if(commandLabel.equalsIgnoreCase(spawnPointExecutor.getCommandName())){
            spawnPointExecutor.execute(player, args);
        }
        else if(commandLabel.equalsIgnoreCase(levelRegionExecutor.getCommandName())){
            levelRegionExecutor.execute(player, args);
        }
        else{
            sendHelpMessage(player);
        }

        return true;
    }

    private void sendHelpMessage(Player player){
        OutputHandler.PrintMessage(player, "Command List");

        String commandPrefix = "/lrm ";
        OutputHandler.PrintRawInfo(player, commandPrefix + spawnPointExecutor.getCommandName() + " - " + spawnPointExecutor.getCommandDescription());
        OutputHandler.PrintRawInfo(player, commandPrefix + levelRegionExecutor.getCommandName() + " - " + levelRegionExecutor.getCommandDescription());
    }
}
