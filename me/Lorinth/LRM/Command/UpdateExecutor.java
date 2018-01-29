package me.Lorinth.LRM.Command;

import me.Lorinth.LRM.Command.Objects.CustomCommandExecutor;
import me.Lorinth.LRM.LorinthsRpgMobs;
import me.Lorinth.LRM.Objects.OutputHandler;
import me.Lorinth.LRM.Updater;
import org.bukkit.entity.Player;

/**
 * Created by lorinthio on 1/29/2018.
 */
public class UpdateExecutor extends CustomCommandExecutor{


    public UpdateExecutor() {
        super("update", "updates LorinthsRpgMobs if available", null);
    }

    @Override
    public void safeExecute(Player player, String[] args) {
        OutputHandler.PrintCommandInfo(player, "Update starting...");
        LorinthsRpgMobs.ForceUpdate(updater ->
        {
            if(updater.getResult() == Updater.UpdateResult.NO_UPDATE)
                OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "You already have the latest version!");
            else if(updater.getResult() == Updater.UpdateResult.SUCCESS)
                OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Update finished! Restart server for the update to take effect!");
            else
                OutputHandler.PrintCommandInfo(player, "[LorinthsRpgMobs] : " + OutputHandler.HIGHLIGHT + "Update Result = " + updater.getResult().toString());
        });
    }

    @Override
    public void sendHelpMessage(Player player) {
        OutputHandler.PrintWhiteSpace(player, 2);
        String prefix = "/" + CommandConstants.LorinthsRpgMobsCommand + " ";
        OutputHandler.PrintCommandInfo(player, prefix + this.getUserFriendlyCommandText());
    }
}
