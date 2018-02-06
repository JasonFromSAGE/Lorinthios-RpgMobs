package me.Lorinth.LRM.Command.Objects;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Util.OutputHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Object used to control commands within LRM
 */
public abstract class CustomCommandExecutor {

    private final String CommandName;
    private final String CommandDescription;
    protected int RequiredArguments = 0;
    private final ArrayList<CustomCommandArgument> CommandArguments; // Argument name, isRequired

    public CustomCommandExecutor(String commandName, String commandDescription, ArrayList<CustomCommandArgument> commandArguments){
        CommandName = commandName;
        CommandDescription = commandDescription;
        CommandArguments = commandArguments;

        if(commandArguments != null)
            for(CustomCommandArgument arg : commandArguments)
                if(arg.isRequired())
                    RequiredArguments ++;
    }

    public abstract void safeExecute(Player player, String[] args);
    public abstract void sendHelpMessage(Player player);

    public String getCommandName(){
        return CommandName;
    }

    public String getCommandDescription(){
        return CommandDescription;
    }

    public int getNumberOfRequiredArguments(){
        return RequiredArguments;
    }

    public ArrayList<CustomCommandArgument> getCommandArguments(){
        return CommandArguments;
    }

    private String getUserFriendlyCommandArguments(){
        if(CommandArguments == null)
            return " ";

        String friendlyText = "";
        for(CustomCommandArgument arg : CommandArguments)
            friendlyText += arg.getLabelWithTag() + " ";

        return " " + friendlyText.trim();
    }

    public String getUserFriendlyCommandText(){
        String userFriendlyArgs = getUserFriendlyCommandArguments();
        return getCommandName() + userFriendlyArgs + OutputHandler.INFO + CommandConstants.DescriptionDelimeter + getCommandDescription();
    }

    protected void handleError(Exception exception, Player player){
        OutputHandler.PrintRawError("Error occurred...");
        exception.printStackTrace();
        OutputHandler.PrintRawError(player, "An error occurred");
        sendHelpMessage(player);
    }

    public void execute(Player player, String[] args){
        try{
            if(args == null || args.length < getNumberOfRequiredArguments()){
                sendHelpMessage(player);
                return;
            }
            safeExecute(player, args);
        }
        catch(Exception exception){
            handleError(exception, player);
        }
    }

    protected void sendCommandArgumentDetails(Player player){
        for(CustomCommandArgument arg : this.getCommandArguments()){
            OutputHandler.PrintCommandInfo(player, CommandConstants.DescriptionDelimeter + arg.getLabel() + OutputHandler.HIGHLIGHT + CommandConstants.DescriptionDelimeter + arg.getDescription());
        }
    }
}
