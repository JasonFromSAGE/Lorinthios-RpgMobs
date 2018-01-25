package me.Lorinth.LRM;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class LorinthsRpgMobs extends JavaPlugin{

    protected static ChatColor Error = ChatColor.RED;
    protected static ChatColor Info = ChatColor.GOLD;
    protected static ChatColor Highlight = ChatColor.GOLD;
    private static String consolePrefix = "[LorinthsRpgMobs] : ";
    private static ConsoleCommandSender console;

    private DataLoader dataLoader;

    @Override
    public void onEnable(){
        PrintMessage("Enabling...");
        console = Bukkit.getConsoleSender();

        loadConfiguration();

        dataLoader = new DataLoader(getConfig());
        Bukkit.getPluginManager().registerEvents(new CreatureEventListener(dataLoader), this);
        PrintMessage("Finished!");
    }

    @Override
    public void onDisable(){
        PrintMessage("Disabling...");

        //Load possible changes in the file from user
        reloadConfig();
        //Apply the changes we gained during the session
        dataLoader.SaveDirtyObjects(getConfig());
        saveConfig();
    }

    public static int GetLevelOfCreature(Creature creature){
        return creature.getMetadata("Level").get(0).asInt();
    }

    protected static void PrintMessage(String message){
        console.sendMessage(Info + consolePrefix + message);
    }

    private void loadConfiguration(){
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }
    }
}
