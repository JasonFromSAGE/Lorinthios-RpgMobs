package me.Lorinth.LRM.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.Console;

/**
 * Created by lorinthio on 1/25/2018.
 */
public class OutputHandler {
    public static final ChatColor ERROR = ChatColor.RED;
    public static final ChatColor INFO = ChatColor.GOLD;
    public static final ChatColor COMMAND = ChatColor.DARK_AQUA;
    public static final ChatColor HIGHLIGHT = ChatColor.GOLD;
    private static String consolePrefix = "[LorinthsRpgMobs] : ";
    private static String infoPrefix = INFO + consolePrefix;
    private static String errorPrefix = ERROR + "[Error]" + infoPrefix + ERROR;
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    /**
     * Should only be used internally in LorinthsRpgMobs as it is used for printing info messages
     * @param message - message to display
     */
    public static void PrintMessage(String message){
        console.sendMessage(infoPrefix + message);
    }

    /**
     * Should only be used internally in LorinthsRpgMobs as it is used for printing error messages
     * @param message - error message to display
     */
    public static void PrintError(String message){
        console.sendMessage(errorPrefix + message);
    }

    public static void PrintRawInfo(String message){ console.sendMessage(INFO + message); }

    public static void PrintRawError(String message){ console.sendMessage(ERROR + message); }

    public static void PrintMessage(Player player, String message){
        player.sendMessage(infoPrefix + message);
    }

    public static void PrintError(Player player, String message){
        player.sendMessage(errorPrefix + message);
    }

    public static void PrintRawInfo(Player player, String message){
        player.sendMessage(INFO + message);
    }

    public static void PrintRawError(Player player, String message){
        player.sendMessage(ERROR + message);
    }

    public static void PrintCommandInfo(Player player, String message){
        player.sendMessage(COMMAND + message);
    }

    public static void PrintWhiteSpace(Player player, int lines){
        for(int i=0; i<lines; i++)
            player.sendMessage("");
    }
}
