package me.Lorinth.LRM.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.io.Console;

/**
 * Created by bnra2 on 1/25/2018.
 */
public class ConsoleOutput {
    public static ChatColor Error = ChatColor.RED;
    public static ChatColor Info = ChatColor.GOLD;
    public static ChatColor Highlight = ChatColor.GOLD;
    private static String consolePrefix = "[LorinthsRpgMobs] : ";
    private static String infoPrefix = Info + consolePrefix;
    private static String errorPrefix = Error + "[Error]" + infoPrefix + Error;
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void PrintMessage(String message){
        console.sendMessage(infoPrefix + message);
    }

    public static void PrintError(String message){
        console.sendMessage(errorPrefix + message);
    }

}
