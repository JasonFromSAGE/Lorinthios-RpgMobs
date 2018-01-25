package me.Lorinth.LRM.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.io.Console;

/**
 * Created by lorinthio on 1/25/2018.
 */
public class ConsoleOutput {
    public static final ChatColor ERROR = ChatColor.RED;
    public static final ChatColor INFO = ChatColor.GOLD;
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

}
