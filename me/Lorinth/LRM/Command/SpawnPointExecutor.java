package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.Command.SpawnPoint.ListSpawnPointExecutor;
import me.Lorinth.LRM.Command.SpawnPoint.RemoveSpawnPointExecutor;
import me.Lorinth.LRM.Command.SpawnPoint.SetSpawnPointExecutor;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by bnra2 on 1/27/2018.
 */
public class SpawnPointExecutor extends CustomCommandExecutor {

    private final ListSpawnPointExecutor listSpawnPointExecutor = new ListSpawnPointExecutor(this);
    private final RemoveSpawnPointExecutor removeSpawnPointExecutor = new RemoveSpawnPointExecutor(this);
    private final SetSpawnPointExecutor setSpawnPointExecutor = new SetSpawnPointExecutor(this);

    public SpawnPointExecutor(){
        super("spawnpoint","access to editing spawnpoint data", null);
    }

    public void execute(Player player, String[] args){
        if(args == null || args.length < 1){
            sendHelpMessage(player);
            return;
        }

        String commandLabel = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        if(commandLabel.equalsIgnoreCase(listSpawnPointExecutor.getCommandName())){
            listSpawnPointExecutor.execute(player, args);
        }
        else if(commandLabel.equalsIgnoreCase(setSpawnPointExecutor.getCommandName())){
            setSpawnPointExecutor.execute(player, args);
        }
        else if(commandLabel.equalsIgnoreCase(removeSpawnPointExecutor.getCommandName())){
            removeSpawnPointExecutor.execute(player, args);
        }
        else{
            sendHelpMessage(player);
        }
    }

    private void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " ";
        OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Spawn Point Command List");
        OutputHandler.PrintCommandInfo(player, prefix + this.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + listSpawnPointExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + setSpawnPointExecutor.getUserFriendlyCommandText());
        OutputHandler.PrintCommandInfo(player, prefix + this.getCommandName() + " " + removeSpawnPointExecutor.getUserFriendlyCommandText());
    }
}
