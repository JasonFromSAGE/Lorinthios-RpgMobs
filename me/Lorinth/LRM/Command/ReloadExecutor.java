package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.entity.Player;

public class ReloadExecutor extends CustomCommandExecutor {

    public ReloadExecutor(){
        super("reload","reloads the plugin", null);
    }

    @Override
    public void safeExecute(Player player, String[] args){
        OutputHandler.PrintInfo(player, "Reloading...");
        LorinthsRpgMobs.Reload();
        OutputHandler.PrintInfo(player, "Reloaded!");
    }

    @Override
    public void sendHelpMessage(Player player){
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " ";
        OutputHandler.PrintCommandInfo(player, prefix + this.getUserFriendlyCommandText());
    }
}
