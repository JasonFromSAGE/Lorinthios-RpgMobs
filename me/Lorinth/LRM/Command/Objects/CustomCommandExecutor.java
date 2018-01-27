package me.Lorinth.LRM.Command.Objects;

import me.Lorinth.LRM.Command.CommandConstants;
import me.Lorinth.LRM.Objects.OutputHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by lorinthio on 1/27/2018.
 */
public abstract class CustomCommandExecutor {

    private final String CommandName;
    private final String CommandDescription;
    private int RequiredArguments = 0;
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

    public String getCommandName(){
        return CommandName;
    };
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
    public abstract void execute(Player player, String[] args);
}
