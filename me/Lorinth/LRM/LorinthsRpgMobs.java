package me.Lorinth.LRM;

import me.Lorinth.LRM.Objects.ConsoleOutput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Created by lorinthio on 1/24/2018.
 */
public class LorinthsRpgMobs extends JavaPlugin{

    private DataLoader dataLoader;

    @Override
    public void onEnable(){
        ConsoleOutput.PrintMessage("Enabling...");
        loadConfiguration();

        dataLoader = new DataLoader(getConfig());
        Bukkit.getPluginManager().registerEvents(new CreatureEventListener(dataLoader), this);
        ConsoleOutput.PrintMessage("Finished!");
    }

    @Override
    public void onDisable(){
        ConsoleOutput.PrintMessage("Disabling...");

        //Load possible changes in the file from user
        reloadConfig();
        //Apply the changes we gained during the session
        dataLoader.SaveDirtyObjects(getConfig());
        saveConfig();
    }

    public static int GetLevelOfCreature(Creature creature){
        return creature.getMetadata("Level").get(0).asInt();
    }

    private void loadConfiguration(){
        if(!new File(getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }
    }
}
